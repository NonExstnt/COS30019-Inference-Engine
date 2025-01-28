import java.util.*;

public class ForwardChaining {
    // Main method to check if the query can be entailed using the forward chaining method
    public static List<String> check(KnowledgeBase kb, String query) {
        Set<String> inferred = new HashSet<>();
        Queue<String> agenda = new LinkedList<>();
        Map<String, Integer> count = new HashMap<>();
        List<String> entailed = new ArrayList<>();

        // Initialize agenda and count
        for (String clause : kb.getClauses()) {
            String[] parts = clause.split("=>");
            if (parts.length == 2) {
                String[] premises = parts[0].split("&");
                count.put(clause, premises.length);
                for (String premise : premises) {
                    premise = premise.trim();
                    if (!agenda.contains(premise)) {
                        agenda.add(premise);
                    }
                }
            } else {
                agenda.add(parts[0].trim());
            }
        }

        // Process the agenda
        while (!agenda.isEmpty()) {
            String p = agenda.poll();
            if (p.equals(query)) {
                entailed.add(p);
                return entailed;
            }
            if (!inferred.contains(p)) {
                inferred.add(p);
                entailed.add(p);
                for (String clause : kb.getClauses()) {
                    String[] parts = clause.split("=>");
                    if (parts.length == 2) {
                        String[] premises = parts[0].split("&");
                        if (Arrays.asList(premises).contains(p)) {
                            count.put(clause, count.get(clause) - 1);
                            if (count.get(clause) == 0) {
                                String conclusion = parts[1].trim();
                                if (!agenda.contains(conclusion)) {
                                    agenda.add(conclusion);
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}