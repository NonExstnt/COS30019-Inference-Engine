import java.util.*;

public class ForwardChaining {
    // Main method to check if the query can be entailed using the forward chaining method
    public static List<String> check(KnowledgeBase kb, String query) {
        Set<String> inferred = new LinkedHashSet<>();
        Queue<String> agenda = new LinkedList<>();
        Map<String, Integer> count = new HashMap<>();
        Map<String, List<String>> implications = new HashMap<>();
        List<String> entailed = new ArrayList<>();

        // Initialize agenda with known facts
        for (String clause : kb.getClauses()) {
            if (!clause.contains("=>")) {
                agenda.add(clause.trim());
            }
        }

        // Initialize count and implications for each clause
        for (String clause : kb.getClauses()) {
            if (clause.contains("=>")) {
                String[] parts = clause.split("=>");
                String[] premises = parts[0].split("&");
                count.put(clause, premises.length);
                for (String premise : premises) {
                    premise = premise.trim();
                    if (!implications.containsKey(premise)) {
                        implications.put(premise, new ArrayList<>());
                    }
                    implications.get(premise).add(clause);
                }
            }
        }

        // Process the agenda
        while (!agenda.isEmpty()) {
            String p = agenda.poll();
            if (p.equals(query)) {
                inferred.add(p);
                entailed.add(p);
                return entailed;
            }
            if (!inferred.contains(p)) {
                inferred.add(p);
                entailed.add(p);
                if (implications.containsKey(p)) {
                    for (String clause : implications.get(p)) {
                        count.put(clause, count.get(clause) - 1);
                        if (count.get(clause) == 0) {
                            String[] parts = clause.split("=>");
                            String conclusion = parts[1].trim();
                            agenda.add(conclusion);
                        }
                    }
                }
            }
        }
        return null;
    }
}