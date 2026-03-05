import java.util.*;

class AnalyticsDashboard {

    private HashMap<String, Integer> pageViews;
    private HashMap<String, Set<String>> uniqueVisitors;
    private HashMap<String, Integer> trafficSources;

    public AnalyticsDashboard() {
        pageViews = new HashMap<>();
        uniqueVisitors = new HashMap<>();
        trafficSources = new HashMap<>();
    }

    public void processEvent(String url, String userId, String source) {

        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    public void getDashboard() {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        pq.addAll(pageViews.entrySet());

        System.out.println("Top Pages:");

        int rank = 1;

        while (!pq.isEmpty() && rank <= 10) {

            Map.Entry<String, Integer> entry = pq.poll();
            String page = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(page).size();

            System.out.println(rank + ". " + page + " - " + views +
                    " views (" + unique + " unique)");

            rank++;
        }

        System.out.println("\nTraffic Sources:");

        for (Map.Entry<String, Integer> entry : trafficSources.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

public class HashTables {

    public static void main(String[] args) {

        AnalyticsDashboard dashboard = new AnalyticsDashboard();

        dashboard.processEvent("/article/breaking-news", "user_123", "google");
        dashboard.processEvent("/article/breaking-news", "user_456", "facebook");
        dashboard.processEvent("/sports/championship", "user_222", "google");
        dashboard.processEvent("/sports/championship", "user_333", "direct");
        dashboard.processEvent("/sports/championship", "user_444", "google");
        dashboard.processEvent("/article/breaking-news", "user_789", "direct");

        dashboard.getDashboard();
    }
}