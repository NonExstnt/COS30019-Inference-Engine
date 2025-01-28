public class InferenceEngine {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: iengine <method> <filename>");
            return;
        }

        String method = args[0];
        String filename = args[1];

        KnowledgeBase kb = new KnowledgeBase();
        kb.loadFromFile(filename);

        String query = kb.getQuery();

        boolean result = false;
        switch (method) {
            case "TT":
                result = TruthTable.check(kb, query);
                break;
            case "FC":
                result = ForwardChaining.check(kb, query);
                break;
            case "BC":
                result = BackwardChaining.check(kb, query);
                break;
            default:
                System.out.println("Unknown method: " + method);
                return;
        }

        System.out.println(result ? "YES" : "NO");
    }
}