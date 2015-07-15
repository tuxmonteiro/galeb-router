package io.galeb.core.util.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ConnectionMapManager {

    public static final String PROP_CMM_TTL_THREAD_ID = "io.galeb.cmm.ttl.threadId";
    public static final String PROP_CMM_TTL_URI       = "io.galeb.cmm.ttl.uri";

    static {
        if (System.getProperty(PROP_CMM_TTL_THREAD_ID)==null) {
            System.setProperty(PROP_CMM_TTL_THREAD_ID, "3600");
        }
        if (System.getProperty(PROP_CMM_TTL_URI)==null) {
            System.setProperty(PROP_CMM_TTL_URI, "3600");
        }
    }

    public static final ConnectionMapManager INSTANCE = new ConnectionMapManager();

    private final ConcurrentHashMapExpirable<String, ConcurrentHashMapExpirable<String, Integer>> uris =
            new ConcurrentHashMapExpirable<>(Long.valueOf(System.getProperty(PROP_CMM_TTL_URI)), TimeUnit.SECONDS, 16, 0.9f, 1);

    private ConnectionMapManager() {
        // SINGLETON
    }

    public ConcurrentHashMapExpirable<String, ConcurrentHashMapExpirable<String, Integer>> getUris() {
        return uris;
    }

    public ConcurrentHashMapExpirable<String, Integer> getCounterMap(String uri) {
        uris.putIfAbsent(uri, new ConcurrentHashMapExpirable<>(Long.valueOf(System.getProperty(PROP_CMM_TTL_THREAD_ID)), TimeUnit.SECONDS, 16, 0.9f, 1));
        return uris.get(uri);
    }

    public void putOnCounterMap(String uri, String key, int count) {
        getCounterMap(uri).put(key, count);
    }

    public int sum(String uri) {
        try {
            return getCounterMap(uri).reduceValuesToInt();
        } catch (NullPointerException e) {
            // Ignore. Already finished.
        }
        return 0;
    }

    public void clear() {
        uris.clear();
    }

    public Map<String, Integer> reduce() {
        final Map<String, Integer> map = new HashMap<>();
        uris.forEach((k, v) -> map.put(k, sum(k)));
        return Collections.unmodifiableMap(map);
    }
}
