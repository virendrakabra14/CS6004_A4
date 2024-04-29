import java.util.*;
import soot.*;

public class PA4 {
    public static void main(String[] args) {
        String classPath = args[0];
        String mainClass = args[1];

        // Set up arguments for Soot
        List<String> sootArgsList = new ArrayList<>(Arrays.asList(
            "-cp", classPath, "-pp",    // sets the class path for Soot
            "-main-class", mainClass    // specify the main class
        ));

        // Add classes to analyse
        for(int i = 1; i < args.length; i++) {
            sootArgsList.add(args[i]);
        }

        // Create transformer for analysis
        AnalysisTransformer analysisTransformer = new AnalysisTransformer();

        // Add transformer to appropriate pack in PackManager; PackManager will run all packs when soot.Main.main is called
        PackManager.v().getPack("jtp").add(new Transform("jtp.dfa", analysisTransformer));

        // Call Soot's main method with arguments
        soot.Main.main(sootArgsList.stream().toArray(String[]::new));
    }
}
