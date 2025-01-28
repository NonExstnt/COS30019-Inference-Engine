import java.util.*;

public class BackwardChaining {
    // Main method to check if the query can be entailed using the backward chaining method
    public static List<String> check(KnowledgeBase kb, String query) {
        Set<String> visited = new LinkedHashSet<>();
        List<String> result = new ArrayList<>();
        if (backwardChain(kb, query, visited, result)) {
            return result;
        } else {
            return null;
        }
    }

    // Recursive method to perform backward chaining
    private static boolean backwardChain(KnowledgeBase kb, String q, Set<String> visited, List<String> result) {
        if (visited.contains(q)) {
            return false;
        }
        visited.add(q);

        for (String clause : kb.getClauses()) {
            if (!clause.contains("=>")) {
                if (clause.trim().equals(q)) {
                    result.add(q);
                    return true;
                }
            } else {
                String[] parts = clause.split("=>");
                String[] premises = parts[0].split("&");
                String conclusion = parts[1].trim();
                if (conclusion.equals(q)) {
                    boolean allPremisesTrue = true;
                    for (String premise : premises) {
                        if (!backwardChain(kb, premise.trim(), visited, result)) {
                            allPremisesTrue = false;
                            break;
                        }
                    }
                    if (allPremisesTrue) {
                        result.add(q);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}