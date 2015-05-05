package io.galeb.core.eventbus;

import io.galeb.core.controller.EntityController.Action;
import io.galeb.core.mapreduce.MapReduce;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Metrics;

public interface IEventBus {

    public static final IEventBus NULL = new NullEventBus();

    public void publishEntity(Entity entity, String entityType, Action action);

    public void onRequestMetrics(Metrics metrics);

    public void onConnectionsMetrics(Metrics metrics);

    public IEventBus setEventBusListener(EventBusListener eventBusListener);

    public void start();

    public MapReduce getMapReduce();

}
