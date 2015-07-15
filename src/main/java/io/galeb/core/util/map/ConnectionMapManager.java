package io.galeb.core.util.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ConnectionMapManager {

    public static final ConnectionMapManager INSTANCE = new ConnectionMapManager();

    public static final String PROP_CMM_TTL_THREAD_ID = "io.galeb.cmm.ttl.threadId";
    public static final String PROP_CMM_URI           = "io.galeb.cmm.ttl.uri";

    private static final long TTL_THREAD_ID = Long.parseLong(System.getProperty(PROP_CMM_TTL_THREAD_ID, Long.toString(3600)));
    private static final long TTL_URI       = Long.parseLong(System.getProperty(PROP_CMM_URI, Long.toString(3600)));

    private final ConcurrentHashMapExpirable<String, ConcurrentHashMapExpirable<String, Integer>> uris =
            new ConcurrentHashMapExpirable<>(TTL_URI, TimeUnit.SECONDS, 16, 0.9f, 1);

    private ConnectionMapManager() {
        // SINGLETON
    }

    public ConcurrentHashMapExpirable<String, ConcurrentHashMapExpirable<String, Integer>> getUris() {
        return uris;
    }

    public ConcurrentHashMapExpirable<String, Integer> getCounterMap(String uri) {
        uris.putIfAbsent(uri, new ConcurrentHashMapExpirable<>(TTL_THREAD_ID, TimeUnit.SECONDS, 16, 0.9f, 1));
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
