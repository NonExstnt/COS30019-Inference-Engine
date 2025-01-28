import java.util.*;

public class TruthTable {
    // Main method to check if the query can be entailed using the truth table method
    public static int check(KnowledgeBase kb, String query) {
        List<String> symbols = getSymbols(kb);
        int modelCount = 0;
        boolean isEntailed = true;

        // Generate all possible models
        int totalModels = (int) Math.pow(2, symbols.size());
        for (int i = 0; i < totalModels; i++) {
            Map<String, Boolean> model = new HashMap<>();
            for (int j = 0; j < symbols.size(); j++) {
                model.put(symbols.get(j), (i & (1 << j)) != 0);
            }
            if (isModelSatisfiable(kb, model)) {
                modelCount++;
                if (!model.getOrDefault(query, false)) {
                    isEntailed = false;
                }
            }
        }

        if (isEntailed) {
            return modelCount;
        } else {
            return 0;
        }
    }

    // Extract all unique symbols from the knowledge base
    private static List<String> getSymbols(KnowledgeBase kb) {
        Set<String> symbols = new HashSet<>();
        for (String clause : kb.getClauses()) {
            String[] parts = clause.split("=>|&|;");
            for (String part : parts) {
                part = part.trim();
                if (!part.isEmpty() && !part.equals("true") && !part.equals("false")) {
                    symbols.add(part);
                }
            }
        }
        return new ArrayList<>(symbols);
    }

    // Check if a given model satisfies the knowledge base
    private static boolean isModelSatisfiable(KnowledgeBase kb, Map<String, Boolean> model) {
        for (String clause : kb.getClauses()) {
            if (clause.contains("=>")) {
                String[] parts = clause.split("=>");
                String[] premises = parts[0].split("&");
                String conclusion = parts[1].trim();
                boolean premisesTrue = true;
                for (String premise : premises) {
                    premise = premise.trim();
                    if (!model.getOrDefault(premise, false)) {
                        premisesTrue = false;
                        break;
                    }
                }
                if (premisesTrue && !model.getOrDefault(conclusion, false)) {
                    return false;
                }
            } else {
                String fact = clause.trim();
                if (!model.getOrDefault(fact, false)) {
                    return false;
                }
            }
        }
        return true;
    }
}