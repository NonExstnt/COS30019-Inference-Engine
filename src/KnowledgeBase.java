import java.io.*;
import java.util.*;

public class KnowledgeBase {
    private List<String> clauses;
    private String query;

    public KnowledgeBase() {
        clauses = new ArrayList<>();
    }

    public void loadFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isQuery = false;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("TELL")) {
                    isQuery = false;
                } else if (line.equals("ASK")) {
                    isQuery = true;
                } else if (!line.isEmpty()) {
                    if (isQuery) {
                        query = line;
                    } else {
                        clauses.addAll(Arrays.asList(line.split(";")));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getClauses() {
        return clauses;
    }

    public String getQuery() {
        return query;
    }
}