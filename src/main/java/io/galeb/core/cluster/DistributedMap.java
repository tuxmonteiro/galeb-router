package io.galeb.core.cluster;

import io.galeb.core.mapreduce.MapReduce;

import java.util.concurrent.ConcurrentMap;

public interface DistributedMap<K, V> {

    public static final String BACKEND_CONNECTIONS = "backendConnections";

    default ConcurrentMap<K, V> getMap(String key) {
        throw new UnsupportedOperationException();
    }

    default void remove(String key) {
        throw new UnsupportedOperationException();
    }

    default void registerListener(final DistributedMapListener distributedMapListener) {
        throw new UnsupportedOperationException();
    }

    default void unregisterListener(final DistributedMapListener distributedMapListener) {
        throw new UnsupportedOperationException();
    }

    default MapReduce getMapReduce() {
        throw new UnsupportedOperationException();
    }

    default DistributedMapStats getStats() {
        throw new UnsupportedOperationException();
    }

}
