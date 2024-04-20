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
    static boolean DEBUG = true;

    private void printDebug(Object s) {
        if(DEBUG) { System.out.println(s); }
    }

    private Unit handleConstant(
        Constant constOp,
        Value otherOp,
        boolean isEq,
        JIfStmt defaultUnit,
        NullnessAnalysis nullnessAnalysis
    ) {
        /*
         * Remove the condition check since
         * one of the arguments is constant
         */

        printDebug("constOp="+constOp);
        printDebug("otherOp="+otherOp);

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
                printDebug(nullnessAnalysis.isAlwaysNullBefore(defaultUnit, otherOpImmediate));
                printDebug(nullnessAnalysis.isAlwaysNonNullBefore(defaultUnit, otherOpImmediate));
                if(nullnessAnalysis.isAlwaysNullBefore(defaultUnit, otherOpImmediate)) {
                    // both operands are null
                    if(isEq) {
                        Stmt target = defaultUnit.getTarget();
                        retUnit = new JGotoStmt(target);
                    }
                    else {
                        retUnit = new JNopStmt();
                    }
                }
                else if(nullnessAnalysis.isAlwaysNonNullBefore(defaultUnit, otherOpImmediate)) {
                    // one operand is null, other is not
                    // do opposite of previous block
                    if(!isEq) {
                        Stmt target = defaultUnit.getTarget();
                        retUnit = new JGotoStmt(target);
                    }
                    else {
                        retUnit = new JNopStmt();
                    }
                }
            }
        }

        return retUnit;
    }

    private Unit handleJIfStmt(
        JIfStmt ifStmt,
        NullnessAnalysis nullnessAnalysis
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
                retUnit = handleConstant(constOp1, op2, isEq, ifStmt, nullnessAnalysis);
            }
            else if(op2 instanceof Constant) {
                Constant constOp2 = (Constant)op2;
                // same function works due to commutativity of eq and neq
                retUnit = handleConstant(constOp2, op1, isEq, ifStmt, nullnessAnalysis);
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
        NullnessAnalysis nullnessAnalysis = new NullnessAnalysis(new CompleteUnitGraph(body));

        // Iterate over all units (instructions) in the method body
        PatchingChain<Unit> units = body.getUnits();

        LinkedList<Unit> worklist = new LinkedList<>();
        for(Unit u: units) {
            worklist.addLast(u);
        }

        while (worklist.size() != 0) {

            Unit unit = worklist.removeFirst();
            printDebug(unit + ":");

            if(unit instanceof JIfStmt) {
                printDebug("ifstmt");
                JIfStmt ifStmt = (JIfStmt)unit;
                Unit unitToPut = handleJIfStmt(ifStmt, nullnessAnalysis);
                if(unit != unitToPut) {
                    printDebug("swapping!!");
                    units.swapWith(unit, unitToPut);
                    // TODO: add successors to worklist?
                }
            }

            printDebug("");
        }
    }
}
