package io.galeb.core.cluster;

import io.galeb.core.model.Entity;

import java.util.Map.Entry;

public interface DistributedMapListener {

    default void mapCleared(String mapName) {
        throw new UnsupportedOperationException();
    }

    default void mapEvicted(String mapName) {
        throw new UnsupportedOperationException();
    }

    default void entryEvicted(Entry<String, Entity> entry) {
        throw new UnsupportedOperationException();
    }

    default void entryUpdated(Entry<String, Entity> entry) {
        throw new UnsupportedOperationException();
    }

    default void entryRemoved(Entry<String, Entity> entry) {
        throw new UnsupportedOperationException();
    }

    default void entryAdded(Entry<String, Entity> entry) {
        throw new UnsupportedOperationException();
    }

}
