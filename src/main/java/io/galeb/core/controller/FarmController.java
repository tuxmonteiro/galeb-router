package io.galeb.core.controller;

import io.galeb.core.json.JsonObject;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Farm;
import io.galeb.core.model.Rule;
import io.galeb.core.model.VirtualHost;

import java.util.Map;

public class FarmController extends EntityController {

    private final BackendController backendController;
    private final BackendPoolController backendPoolController;
    private final RuleController ruleController;
    private final VirtualHostController virtualHostController;

    public FarmController(final Farm farm, final Map<String, EntityController> entityControllerMap) {
        super(farm);
        backendController = (BackendController) entityControllerMap
                .getOrDefault(getControllerName(BackendController.class), NULL);
        backendPoolController = (BackendPoolController) entityControllerMap
                .getOrDefault(getControllerName(BackendPoolController.class), NULL);
        ruleController = (RuleController) entityControllerMap
                .getOrDefault(getControllerName(RuleController.class), NULL);
        virtualHostController = (VirtualHostController) entityControllerMap
                .getOrDefault(getControllerName(VirtualHostController.class), NULL);
    }

    @Deprecated @Override
    public EntityController add(JsonObject json) throws Exception {
        final Farm farmAdded = (Farm) json.instanceOf(Farm.class);
        return add(farmAdded);
    }

    @Override
    public EntityController add(Entity entity) throws Exception {
        for (Entity backendPool : ((Farm) entity).getCollection(BackendPool.class)) {
            backendPoolController.add(backendPool.copy());
            for (Entity backend : ((BackendPool) backendPool).getBackends()) {
                backendController.add(backend.copy());
            }
        };
        for (Entity virtualhost : ((Farm) entity).getCollection(VirtualHost.class)) {
            virtualHostController.add(virtualhost.copy());
            for (Entity rule : ((VirtualHost) virtualhost).getRules()) {
                ruleController.add(rule.copy());
            }
        };
        setVersion(entity.getVersion());
        return this;
    }

    @Deprecated @Override
    public EntityController del(JsonObject json) throws Exception {
        delAll();
        return this;
    }

    @Override
    public EntityController del(Entity entity) throws Exception {
        delAll();
        return this;
    }

    @Override
    public EntityController delAll() throws Exception {
        delAll(Backend.class);
        delAll(BackendPool.class);
        delAll(Rule.class);
        delAll(VirtualHost.class);
        setVersion(0);
        return this;
    }

    @Deprecated @Override
    public EntityController change(JsonObject json) throws Exception {
        final Farm farmChanged = (Farm) json.instanceOf(Farm.class);
        return change(farmChanged);
    }

    @Override
    public EntityController change(Entity entity) throws Exception {
        for (final Entity backendPool: ((Farm) entity).getCollection(BackendPool.class)) {
            backendPoolController.change(backendPool);
            for (final Backend backend: ((BackendPool) backendPool).getBackends()) {
                backendController.change(backend);
            }
        }
        for (final Entity virtualhost: ((Farm) entity).getCollection(VirtualHost.class)) {
            virtualHostController.change(virtualhost);
            for (final Rule rule: ((VirtualHost) virtualhost).getRules()) {
                ruleController.change(rule);
            }
        }
        setVersion(entity.getVersion());
        return this;
    }

    @Override
    public String get(String id) {
        return get(Farm.class, null);
    }

}
