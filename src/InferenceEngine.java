import java.util.*;

public class InferenceEngine {
    public static void main(String[] args) {

        String method = args[0];
        String filename = args[1];

        if (method.isEmpty() || filename.isEmpty()) {
            System.out.println("Usage: iengine <method> <filename>");
            return;
        }

        KnowledgeBase kb = new KnowledgeBase();
        kb.loadFromFile(filename);

        String query = kb.getQuery();

        switch (method) {
            case "TT":
                int modelCount = TruthTable.check(kb, query);
                System.out.println(modelCount > 0 ? "YES: " + modelCount : "NO");
                break;
            case "FC":
                List<String> fcResult = ForwardChaining.check(kb, query);
                System.out.println(fcResult != null ? "YES: " + String.join(", ", fcResult) : "NO");
                break;
            case "BC":
                List<String> bcResult = BackwardChaining.check(kb, query);
                System.out.println(bcResult != null ? "YES: " + String.join(", ", bcResult) : "NO");
                break;
            default:
                System.out.println("Unknown method: " + method);
                break;
        }
    }
}