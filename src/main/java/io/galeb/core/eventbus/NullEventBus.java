package io.galeb.core.eventbus;

import io.galeb.core.controller.EntityController.Action;
import io.galeb.core.mapreduce.MapReduce;
import io.galeb.core.mapreduce.NullMapReduce;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Metrics;
import io.galeb.core.queue.QueueManager;

import javax.enterprise.inject.Alternative;

@Alternative
public class NullEventBus implements IEventBus {

    public static final MapReduce NULL_MAP_REDUCE = new NullMapReduce();

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

    @Override
    public MapReduce getMapReduce() {
        return NULL_MAP_REDUCE;
    }

    @Override
    public QueueManager getQueueManager() {
        return QueueManager.NULL;
    }

}
