import java.util.*;

class DNSEntry {

    String domain;
    String ipAddress;
    long expiryTime;
    long lastAccess;

    public DNSEntry(String domain, String ipAddress, int ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000L;
        this.lastAccess = System.currentTimeMillis();
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

class DNSCache {

    private int capacity;
    private HashMap<String, DNSEntry> cache;
    private LinkedHashMap<String, String> lruTracker;
    private int hits;
    private int misses;

    public DNSCache(int capacity) {
        this.capacity = capacity;
        cache = new HashMap<>();
        lruTracker = new LinkedHashMap<>(capacity, 0.75f, true);
    }

    private String queryUpstreamDNS(String domain) {
        return "172.217.14." + new Random().nextInt(255);
    }

    public String resolve(String domain) {

        if (cache.containsKey(domain)) {

            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                hits++;
                entry.lastAccess = System.currentTimeMillis();
                lruTracker.get(domain);
                return "Cache HIT → " + entry.ipAddress;
            } else {
                cache.remove(domain);
                lruTracker.remove(domain);
            }
        }

        misses++;

        String ip = queryUpstreamDNS(domain);
        DNSEntry newEntry = new DNSEntry(domain, ip, 5);

        if (cache.size() >= capacity) {
            String lruKey = lruTracker.keySet().iterator().next();
            cache.remove(lruKey);
            lruTracker.remove(lruKey);
        }

        cache.put(domain, newEntry);
        lruTracker.put(domain, ip);

        return "Cache MISS → Query upstream → " + ip;
    }

    public void getCacheStats() {
        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);

        System.out.println("Cache Hits: " + hits);
        System.out.println("Cache Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }
}

public class HashTables {

    public static void main(String[] args) throws InterruptedException {

        DNSCache dnsCache = new DNSCache(5);

        System.out.println("resolve(\"google.com\") → " + dnsCache.resolve("google.com"));
        System.out.println("resolve(\"google.com\") → " + dnsCache.resolve("google.com"));

        Thread.sleep(6000);

        System.out.println("resolve(\"google.com\") → " + dnsCache.resolve("google.com"));

        dnsCache.getCacheStats();
    }
}