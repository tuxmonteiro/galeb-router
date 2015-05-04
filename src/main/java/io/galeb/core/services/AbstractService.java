package io.galeb.core.services;

import io.galeb.core.controller.BackendController;
import io.galeb.core.controller.BackendPoolController;
import io.galeb.core.controller.EntityController;
import io.galeb.core.controller.EntityController.Action;
import io.galeb.core.controller.ListenerController;
import io.galeb.core.controller.RuleController;
import io.galeb.core.controller.VirtualHostController;
import io.galeb.core.eventbus.Event;
import io.galeb.core.eventbus.EventBusListener;
import io.galeb.core.eventbus.IEventBus;
import io.galeb.core.json.JsonObject;
import io.galeb.core.logging.Logger;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Farm;
import io.galeb.core.sched.BackendPoolUpdater;

import java.util.Map;

import javax.inject.Inject;

import org.quartz.SchedulerException;

public abstract class AbstractService implements ListenerController, EventBusListener {

    @Inject
    protected Farm farm;

    @Inject
    protected IEventBus eventbus;

    @Inject
    protected Logger logger;

    private BackendPoolUpdater backendPoolUpdater;

    public AbstractService() {
        super();
    }

    protected void prelaunch() {
        eventbus.setEventBusListener(this).start();
        registerControllers();
        startBackendPoolUpdater();
    }

    protected void registerControllers() {

        final Map<String, EntityController> entityMap = farm.getEntityMap();

        entityMap.put(getControllerName(BackendController.class),
                new BackendController(farm).registerListenerController(this));
        entityMap.put(getControllerName(BackendPoolController.class),
                new BackendPoolController(farm).registerListenerController(this));
        entityMap.put(getControllerName(RuleController.class),
                new RuleController(farm).registerListenerController(this));
        entityMap.put(getControllerName(VirtualHostController.class),
                new VirtualHostController(farm).registerListenerController(this));

    }

    protected void startBackendPoolUpdater() {
        backendPoolUpdater = new BackendPoolUpdater(farm, eventbus, logger);
        try {
            backendPoolUpdater.start();
        } catch (SchedulerException e) {
            logger.error(e);
        }
    }

    private String getControllerName(Class<?> clazz) {
        return clazz.getSimpleName().toLowerCase().replace("controller", "");
    }

    public Farm getFarm() {
        return farm;
    }

    @Override
    public IEventBus getEventBus() {
        return eventbus;
    }

    @Override
    public void handleController(JsonObject json, EntityController.Action action) {
        throw new UnsupportedOperationException(toString());
    }

    @Override
    public void onEvent(Event event) throws RuntimeException {

        final JsonObject json = event.getData();

        final Entity entity = (Entity) json.instanceOf(Entity.class);
        final String entityType = entity.getEntityType();

        EntityController entityController = farm.getEntityMap().get(entityType);
        if (entityController==null) {
            entityController = EntityController.NULL;
            logger.error("EntityController is NULL");
        }

        final Object eventType = event.getType();

        if (eventType instanceof Action) {
            final Action action = (Action) eventType;

            try {
                switch (action) {
                    case ADD:
                        entityController.add(json);
                        break;
                    case DEL:
                        entityController.del(json);
                        break;
                    case CHANGE:
                        entityController.change(json);
                        break;
                    default:
                        throw new RuntimeException("Action unknown");
                }
            } catch (final Exception e) {
                logger.debug(e);
            }
        }

    }

}
