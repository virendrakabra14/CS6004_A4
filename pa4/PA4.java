import java.util.Iterator;
import java.util.List;

import soot.*;
import soot.Body;
import soot.NormalUnitPrinter;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.UnitPrinter;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.jimple.internal.*;

public class PA4 {
    public static void main(String[] args) {
        String classPath = "."; 	// change to appropriate path to the test class

        //Set up arguments for Soot
        String[] sootArgs = {
            "-cp", classPath, "-pp", // sets the class path for Soot
            "-keep-line-number", // preserves line numbers in input Java files
            // "-f", "J",
            // "-p", "jap.npcolorer", "on",
            "-main-class", "Test",	// specify the main class
            "Test", "Node"                  // list the classes to analyze
        };

        // Create transformer for analysis
        AnalysisTransformer analysisTransformer = new AnalysisTransformer();

        // Add transformer to appropriate pack in PackManager; PackManager will run all packs when soot.Main.main is called
        PackManager.v().getPack("jop").remove("cbf");
        PackManager.v().getPack("jop").remove("cpf");
        PackManager.v().getPack("jtp").add(new Transform("jtp.dfa", analysisTransformer));

        // Call Soot's main method with arguments
        soot.Main.main(sootArgs);
    }
}
