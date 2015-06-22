package io.galeb.core.cluster;

import java.util.concurrent.ConcurrentMap;

public interface DistributedMap<K, V> {

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

}
