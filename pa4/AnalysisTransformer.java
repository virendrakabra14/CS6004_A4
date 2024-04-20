import java.util.*;
import soot.*;
import soot.jimple.Stmt;
import soot.jimple.internal.JEqExpr;
import soot.jimple.internal.JGotoStmt;
import soot.jimple.internal.JIfStmt;
import soot.jimple.internal.JNeExpr;
import soot.jimple.internal.JNopStmt;
import soot.jimple.AssignStmt;
import soot.jimple.ConditionExpr;
import soot.jimple.Constant;
import soot.jimple.AddExpr;
import soot.jimple.MulExpr;
import soot.jimple.NullConstant;
import soot.jimple.Jimple;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.jimple.toolkits.annotation.nullcheck.NullnessAnalysis;

public class AnalysisTransformer extends BodyTransformer {
    private Unit handleConstant(
        Constant constOp,
        Value otherOp,
        boolean isEq,
        JIfStmt defaultUnit,
        NullnessAnalysis na
    ) {
        /*
         * Remove the condition check since
         * one of the arguments is constant
         */

        Unit retUnit = defaultUnit;

        if(constOp.equivTo(otherOp)) {
            if(isEq) {
                // " == " and operands are actually equal
                // so add `goto label`
                Stmt target = defaultUnit.getTarget();
                retUnit = new JGotoStmt(target);
            }
            else {
                retUnit = new JNopStmt();
            }
        }
        else {
            // if constant operand is null,
            // we use nullness analysis to remove conditional

            if(!(otherOp instanceof Immediate)) {
                return defaultUnit;
            }

            Immediate otherOpImmediate = (Immediate)otherOp;

            if(constOp instanceof NullConstant) {
                if(na.isAlwaysNullBefore(defaultUnit, otherOpImmediate)) {
                    // TODO
                }
            }
        }

        return retUnit;
    }

    private Unit handleJIfStmt(
        JIfStmt ifStmt,
        NullnessAnalysis na
    ) {
        Unit retUnit = ifStmt; // default: same statement as appears

        /*
         * From Jimple grammar
         * ifStmt -> if conditionExpr goto label;
         * conditionExpr -> imm1 condop imm2
         *
         * and
         * imm -> local | constant
         */

        Value ifCondition = ifStmt.getCondition();
        ConditionExpr ifConditionExpr = (ConditionExpr)ifCondition;

        if(ifConditionExpr instanceof JEqExpr || ifConditionExpr instanceof JNeExpr) {
            boolean isEq = (ifConditionExpr instanceof JEqExpr);

            Value op1 = ifConditionExpr.getOp1();
            Value op2 = ifConditionExpr.getOp2();

            if(!(op1 instanceof Immediate) || !(op2 instanceof Immediate)) {
                return ifStmt;
            }

            if(op1 instanceof Constant) {
                Constant constOp1 = (Constant)op1;
                retUnit = handleConstant(constOp1, op2, isEq, ifStmt, na);
            }
            else if(op2 instanceof Constant) {
                Constant constOp2 = (Constant)op2;
                // same function works due to commutativity of eq and neq
                retUnit = handleConstant(constOp2, op1, isEq, ifStmt, na);
            }
        }

        return retUnit;
    }

    @Override
    synchronized protected void internalTransform(
        Body body,
        String phaseName,
        Map<String, String> options
    ) {
        boolean DEBUG = true;

        NullnessAnalysis na = new NullnessAnalysis(new CompleteUnitGraph(body));

        // Iterate over all units (instructions) in the method body
        PatchingChain<Unit> units = body.getUnits();

        LinkedList<Unit> worklist = new LinkedList<>();
        for(Unit u: units) {
            worklist.addLast(u);
        }

        // Iterate over instructions and replace iadd with imul
        Iterator<Unit> unitIt = units.snapshotIterator();
        while (worklist.size() != 0) {

            Unit unit = worklist.removeFirst();
            if(DEBUG) { System.out.println(unit + ": "); }

            if(unit instanceof JIfStmt) {
                JIfStmt ifStmt = (JIfStmt)unit;
                Unit unitToPut = handleJIfStmt(ifStmt, na);
                if(unit != unitToPut) {
                    units.swapWith(unit, unitToPut);
                    // TODO: add successors to worklist?
                }
            }

        }
    }
}
