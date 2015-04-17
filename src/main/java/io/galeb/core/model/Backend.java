package io.galeb.core.model;

import lombok.NonNull;

import com.google.gson.annotations.Expose;

public class Backend extends Entity {

    private static final long serialVersionUID = 1L;

    public enum Health {
        HEALTHY,
        DEADY,
        UNKNOWN
    }

    @Expose private Health health = Health.HEALTHY;

    public Health getHealth() {
        return health;
    }

    public final void setHealth(@NonNull Health health) {
        this.health = health;
    }

}
