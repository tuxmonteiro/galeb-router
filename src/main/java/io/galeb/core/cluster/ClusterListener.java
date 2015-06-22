package io.galeb.core.cluster;

public interface ClusterListener {

    default void onClusterReady() {
        // default
    }

    default void onClusterExit() {
        // default
    }

}
