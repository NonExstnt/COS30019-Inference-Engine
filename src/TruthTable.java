import java.util.*;

public class TruthTable {
    // Main method to check if the query can be entailed using the truth table method
    public static int check(KnowledgeBase kb, String query) {
        List<String> symbols = extractSymbols(kb);
        return ttCheckAll(kb, query, symbols, new HashMap<>(), 0);
    }

    // Recursive method to check all possible models
    private static int ttCheckAll(KnowledgeBase kb, String query, List<String> symbols, Map<String, Boolean> model, int modelCount) {
        if (symbols.isEmpty()) {
            if (plTrue(kb.getClauses(), model)) {
                if (plTrue(Collections.singletonList(query), model)) {
                    return modelCount + 1;
                }
            }
            return modelCount;
        } else {
            String p = symbols.get(0);
            List<String> rest = symbols.subList(1, symbols.size());
            Map<String, Boolean> trueModel = new HashMap<>(model);
            trueModel.put(p, true);
            Map<String, Boolean> falseModel = new HashMap<>(model);
            falseModel.put(p, false);
            return ttCheckAll(kb, query, rest, trueModel, modelCount) + ttCheckAll(kb, query, rest, falseModel, modelCount);
        }
    }

    // Method to check if a given model satisfies all clauses
    private static boolean plTrue(List<String> clauses, Map<String, Boolean> model) {
        for (String clause : clauses) {
            if (!plTrue(clause, model)) {
                return false;
            }
        }
        return true;
    }

    // Method to check if a given model satisfies a single clause
    private static boolean plTrue(String clause, Map<String, Boolean> model) {
        String[] parts = clause.split("=>");
        if (parts.length == 2) {
            return !plTrue(parts[0], model) || plTrue(parts[1], model);
        } else {
            String[] literals = clause.split("&");
            for (String literal : literals) {
                literal = literal.trim();
                boolean negated = literal.startsWith("~");
                String symbol = negated ? literal.substring(1) : literal;
                Boolean value = model.get(symbol);
                if (value == null || value == negated) {
                    return false;
                }
            }
            return true;
        }
    }

    // Method to extract all unique symbols from the knowledge base
    private static List<String> extractSymbols(KnowledgeBase kb) {
        Set<String> symbols = new HashSet<>();
        for (String clause : kb.getClauses()) {
            String[] parts = clause.split("=>");
            for (String part : parts) {
                String[] literals = part.split("&");
                for (String literal : literals) {
                    literal = literal.trim();
                    if (literal.startsWith("~")) {
                        literal = literal.substring(1);
                    }
                    symbols.add(literal);
                }
            }
        }
        return new ArrayList<>(symbols);
    }
}