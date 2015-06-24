package io.galeb.core.cluster;

import io.galeb.core.model.Entity;

public interface DistributedMapListener {

    default void mapCleared(String mapName) {
        throw new UnsupportedOperationException();
    }

    default void mapEvicted(String mapName) {
        throw new UnsupportedOperationException();
    }

    default void entryEvicted(Entity entity) {
        throw new UnsupportedOperationException();
    }

    default void entryUpdated(Entity entity) {
        throw new UnsupportedOperationException();
    }

    default void entryRemoved(Entity entity) {
        throw new UnsupportedOperationException();
    }

    default void entryAdded(Entity entity) {
        throw new UnsupportedOperationException();
    }

    default void showStatistic() {
        //
    }

}
