public class GetJimple {
    public static void main(String[] args) {
        String classPath = ".";

        String[] sootArgsFwd = {
            "-cp", classPath,
            "-pp",
            "-f", "J",
            "Test", "Node",
            "-p", "jop.cpf", "off",
            "-p", "jop.cbf", "off",
        };
        soot.Main.main(sootArgsFwd);
    }
}
