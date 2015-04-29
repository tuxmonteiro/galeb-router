package io.galeb.core.eventbus;

import io.galeb.core.controller.EntityController.Action;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Metrics;

import javax.enterprise.inject.Alternative;

@Alternative
public class NullEventBus implements IEventBus {

    @Override
    public void publishEntity(Entity entity, String entityType, Action action) {
        // NULL
    }

    @Override
    public void onRequestMetrics(Metrics metrics) {
        // NULL
    }

    @Override
    public void onConnectionsMetrics(Metrics metrics) {
        // NULL
    }

    @Override
    public IEventBus setEventBusListener(EventBusListener eventBusListener) {
        return this;
    }

    @Override
    public void start() {
        // NULL
    }

}
