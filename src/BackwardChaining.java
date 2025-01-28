import java.util.*;

public class BackwardChaining {
    // Main method to check if the query can be entailed using the backward chaining method
    public static List<String> check(KnowledgeBase kb, String query) {
        List<String> entailed = new ArrayList<>();
        if (bcCheck(kb, query, new HashSet<>(), entailed)) {
            return entailed;
        }
        return null;
    }

    // Recursive method to check if the query can be entailed
    private static boolean bcCheck(KnowledgeBase kb, String query, Set<String> visited, List<String> entailed) {
        if (visited.contains(query)) {
            return false;
        }
        visited.add(query);

        for (String clause : kb.getClauses()) {
            String[] parts = clause.split("=>");
            if (parts.length == 2 && parts[1].trim().equals(query)) {
                String[] premises = parts[0].split("&");
                boolean allPremisesTrue = true;
                for (String premise : premises) {
                    premise = premise.trim();
                    if (!bcCheck(kb, premise, visited, entailed)) {
                        allPremisesTrue = false;
                        break;
                    }
                }
                if (allPremisesTrue) {
                    entailed.add(query);
                    return true;
                }
            } else if (parts.length == 1 && parts[0].trim().equals(query)) {
                entailed.add(query);
                return true;
            }
        }
        return false;
    }
}