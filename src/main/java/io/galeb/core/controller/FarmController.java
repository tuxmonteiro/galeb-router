package io.galeb.core.controller;

import io.galeb.core.json.JsonObject;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Farm;
import io.galeb.core.model.Rule;
import io.galeb.core.model.VirtualHost;

public class FarmController implements EntityController {

    private final Farm farm;
    private final EntityController backendPoolController;
    private final EntityController backendController;
    private final EntityController virtualHostController;
    private final EntityController ruleController;

    public FarmController(final Farm farm) {
        this.farm = farm;
        backendPoolController = farm.getEntityMap().get(controllerName(BackendPoolController.class));
        backendController = farm.getEntityMap().get(controllerName(BackendController.class));
        virtualHostController = farm.getEntityMap().get(controllerName(VirtualHostController.class));
        ruleController = farm.getEntityMap().get(controllerName(RuleController.class));
    }

    private String controllerName(Class<? extends EntityController> clazz) {
        return clazz.getSimpleName().toLowerCase().replace("controller", "");
    }

    @Override
    public EntityController add(JsonObject json) throws Exception {
        final Farm farmAdded = (Farm) json.instanceOf(Farm.class);

        for (final BackendPool backendPool: farmAdded.getBackendPools()) {
            backendPoolController.add(JsonObject.toJsonObject(backendPool));
            for (final Backend backend: backendPool.getBackends()) {
                backendController.add(JsonObject.toJsonObject(backend));
            }
        }
        for (final VirtualHost virtualhost: farmAdded.getVirtualHosts()) {
            virtualHostController.add(JsonObject.toJsonObject(virtualhost));
            for (final Rule rule: virtualhost.getRules()) {
                ruleController.add(JsonObject.toJsonObject(rule));
            }
        }
        farm.setVersion(farmAdded.getVersion());
        return this;
    }

    @Override
    public EntityController del(JsonObject json) throws Exception {
        delAll();
        return this;
    }

    @Override
    public EntityController delAll() throws Exception {
        backendController.delAll();
        backendPoolController.delAll();
        ruleController.delAll();
        virtualHostController.delAll();
        farm.setVersion(0);
        return this;
    }

    @Override
    public EntityController change(JsonObject json) throws Exception {
        final Farm farmChanged = (Farm) json.instanceOf(Farm.class);

        for (final BackendPool backendPool: farmChanged.getBackendPools()) {
            backendPoolController.add(JsonObject.toJsonObject(backendPool));
            for (final Backend backend: backendPool.getBackends()) {
                backendController.add(JsonObject.toJsonObject(backend));
            }
        }

        for (final VirtualHost virtualhost: farmChanged.getVirtualHosts()) {
            virtualHostController.add(JsonObject.toJsonObject(virtualhost));
            for (final Rule rule: virtualhost.getRules()) {
                ruleController.add(JsonObject.toJsonObject(rule));
            }
        }
        farm.setVersion(farmChanged.getVersion());
        return this;
    }

    @Override
    public String get(String id) {
        return JsonObject.toJsonString(farm);
    }

}
