package io.galeb.core.eventbus;

import io.galeb.core.controller.EntityController.Action;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Metrics;

public interface IEventBus {

    public static final IEventBus NULL = new NullEventBus();

    public void publishEntity(Entity entity, String entityType, Action action);

    public void sendMetrics(Metrics metrics);

    public IEventBus setEventBusListener(EventBusListener eventBusListener);

    public void start();

}
