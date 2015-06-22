package io.galeb.core.cluster;

public interface ClusterEvents {

    default void registerListener(ClusterListener clusterListener) {

    }

    default boolean isReady() {
        return false;
    };

}
