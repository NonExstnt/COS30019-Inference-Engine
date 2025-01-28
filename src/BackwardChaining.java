import java.util.*;

public class BackwardChaining {
    private KnowledgeBase kb;
    private final String query;
    private final Set<String> inferred;
    private final List<String> agenda;
    private final Map<String, List<String>> clauses;

    public BackwardChaining(KnowledgeBase kb, String query) {
        this.kb = kb;
        this.query = query;
        this.inferred = new LinkedHashSet<>();
        this.agenda = new ArrayList<>();
        this.clauses = new HashMap<>();
        initializeClauses();
    }

    private void initializeClauses() {
        for (String clause : kb.getClauses()) {
            String[] parts = clause.split("=>");
            if (parts.length == 2) { // Ensure the clause has both a body and a head
                String head = parts[1].trim();
                String[] body = parts[0].trim().split("&");

                if (!clauses.containsKey(head)) {
                    clauses.put(head, new ArrayList<>());
                }
                clauses.get(head).addAll(Arrays.asList(body));
            } else if (parts.length == 1) { // Handle facts without "=>"
                String fact = parts[0].trim();
                if (!clauses.containsKey(fact)) {
                    clauses.put(fact, new ArrayList<>());
                }
            } else {
                System.err.println("Invalid clause format: " + clause);
            }
        }
    }

    public static List<String> check(KnowledgeBase kb, String query) {
        BackwardChaining bc = new BackwardChaining(kb, query);
        return bc.infer();
    }

    public List<String> infer() {
        agenda.add(query);
        List<String> result = new ArrayList<>();

        while (!agenda.isEmpty()) {
            String p = agenda.remove(agenda.size() - 1);
            if (!inferred.contains(p)) {
                inferred.add(p);
                result.add(p);

                if (clauses.containsKey(p)) {
                    for (String premise : clauses.get(p)) {
                        if (!premise.isEmpty()) {
                            agenda.add(premise.trim());
                        }
                    }
                } else {
                    return null;
                }
            }
        }

        Collections.reverse(result);
        return result;
    }
}