package io.galeb.core.util.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ConnectionMapManager {

    public static ConnectionMapManager INSTANCE = new ConnectionMapManager();

    private static final long TTL_THREAD_ID = 1L; // hour
    private static final long TTL_URI       = 1L; // hour

    private final ConcurrentHashMapExpirable<String, ConcurrentHashMapExpirable<String, Integer>> uris =
            new ConcurrentHashMapExpirable<>(TTL_URI, TimeUnit.HOURS, 16, 0.9f, 1);

    private ConnectionMapManager() {
        // SINGLETON
    }

    public ConcurrentHashMapExpirable<String, ConcurrentHashMapExpirable<String, Integer>> getUris() {
        return uris;
    }

    public ConcurrentHashMapExpirable<String, Integer> getCounterMap(String uri) {
        uris.putIfAbsent(uri, new ConcurrentHashMapExpirable<>(TTL_THREAD_ID, TimeUnit.HOURS, 16, 0.9f, 1));
        return uris.get(uri);
    }

    public void putOnCounterMap(String uri, String key, int count) {
        getCounterMap(uri).put(key, count);
    }

    public int sum(String uri) {
        return getCounterMap(uri).reduceValuesToInt();
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
