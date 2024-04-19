import java.util.*;
import soot.*;
import soot.jimple.Stmt;
import soot.jimple.AssignStmt;
import soot.jimple.AddExpr;
import soot.jimple.MulExpr;
import soot.jimple.Jimple;
// import soot.toolkits.scalar.;
import soot.jimple.toolkits.annotation.nullcheck.NullPointerColorer; // stmt.getTag("NullCheckTag")
import soot.tagkit.Tag;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.jimple.toolkits.annotation.nullcheck.NullnessAnalysis;

public class AnalysisTransformer extends BodyTransformer {
    @Override
    synchronized protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        boolean DEBUG = true;

        NullnessAnalysis na = new NullnessAnalysis(new CompleteUnitGraph(body));

        // Iterate over all units (instructions) in the method body
        PatchingChain<Unit> units = body.getUnits();
        // Iterate over instructions and replace iadd with imul
        Iterator<Unit> unitIt = units.snapshotIterator();
        while (unitIt.hasNext()) {  // TODO: go to successor(s)?
                                    // TODO: worklist?
            Unit unit = unitIt.next();
            if(DEBUG) { System.out.println(unit + ": "); }
            System.out.println("    " + na.getFlowBefore(unit));
            for(Tag t: unit.getTags()) {
                System.out.println("    " + t.getName() + "=" + t);
            }
            if (unit instanceof Stmt) {
                Stmt stmt = (Stmt) unit;
                // Check if it’s an iadd operation
                if (stmt instanceof AssignStmt) {
                    AssignStmt assignStmt = (AssignStmt) stmt;
                    Value rightOp = assignStmt.getRightOp();
                    if (rightOp instanceof AddExpr && rightOp.getType() instanceof IntType) {
                        AddExpr addExpr = (AddExpr) rightOp;
                        if (addExpr.getOp1().getType() instanceof IntType && addExpr.getOp2().getType
                        () instanceof IntType) {
                            // Create a new multiplication expression
                            MulExpr mulExpr = Jimple.v().newMulExpr(addExpr.getOp1(), addExpr.getOp2());
                            // Replace iadd with imul
                            assignStmt.setRightOp(mulExpr);
                        }
                    }
                }
            }
        }
    }
}
