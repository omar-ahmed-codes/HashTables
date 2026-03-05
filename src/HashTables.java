import java.util.*;

class UsernameAvailabilityChecker {

    private HashMap<String, Integer> usernameMap;
    private HashMap<String, Integer> attemptFrequency;

    public UsernameAvailabilityChecker() {
        usernameMap = new HashMap<>();
        attemptFrequency = new HashMap<>();
    }

    public void registerUser(String username, int userId) {
        usernameMap.put(username, userId);
    }

    public boolean checkAvailability(String username) {
        attemptFrequency.put(username,
                attemptFrequency.getOrDefault(username, 0) + 1);
        return !usernameMap.containsKey(username);
    }

    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        suggestions.add(username + "1");
        suggestions.add(username + "2");

        if (username.contains("_")) {
            suggestions.add(username.replace("_", "."));
        } else {
            suggestions.add(username + "_official");
        }

        return suggestions;
    }

    public String getMostAttempted() {

        String mostAttempted = "";
        int maxAttempts = 0;

        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {

            if (entry.getValue() > maxAttempts) {
                maxAttempts = entry.getValue();
                mostAttempted = entry.getKey();
            }
        }

        return mostAttempted + " (" + maxAttempts + " attempts)";
    }
}

public class HashTables {

    public static void main(String[] args) {

        UsernameAvailabilityChecker checker = new UsernameAvailabilityChecker();

        checker.registerUser("john_doe", 101);
        checker.registerUser("admin", 102);

        System.out.println("checkAvailability(\"john_doe\") → "
                + checker.checkAvailability("john_doe"));

        System.out.println("checkAvailability(\"jane_smith\") → "
                + checker.checkAvailability("jane_smith"));

        System.out.println("suggestAlternatives(\"john_doe\") → "
                + checker.suggestAlternatives("john_doe"));

        checker.checkAvailability("admin");
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");

        System.out.println("getMostAttempted() → "
                + checker.getMostAttempted());
    }
}