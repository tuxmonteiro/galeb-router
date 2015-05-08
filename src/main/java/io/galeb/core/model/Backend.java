package io.galeb.core.model;

import com.google.gson.annotations.Expose;

public class Backend extends Entity {

    private static final long serialVersionUID = 1L;

    public static final String PROP_ACTIVECONN = "activeConn";

    public enum Health {
        HEALTHY,
        DEADY,
        UNKNOWN
    }

    @Expose private Health health = Health.HEALTHY;

    @Expose private int connections = 0;

    public Health getHealth() {
        return health;
    }

    public final void setHealth(Health health) {
        this.health = health;
    }

    public int getConnections() {
        return connections;
    }

    public Backend setConnections(int connections) {
        this.connections = connections;
        return this;
    }

}
