package io.galeb.core.controller;

import io.galeb.core.json.JsonObject;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Farm;
import io.galeb.core.model.Rule;
import io.galeb.core.model.VirtualHost;

public class FarmController implements EntityController {

    private final Farm farm;

    public FarmController(final Farm farm) {
        this.farm = farm;
    }

    @Override
    public EntityController add(JsonObject json) throws Exception {
        final Farm farmAdded = (Farm) json.instanceOf(Farm.class);

        for (final Entity backendPool: farmAdded.getCollection(BackendPool.class)) {
            farm.add(backendPool);
            for (final Backend backend: ((BackendPool) backendPool).getBackends()) {
                farm.add(backend);
            }
        }
        for (final Entity virtualhost: farmAdded.getCollection(VirtualHost.class)) {
            farm.add(virtualhost);
            for (final Rule rule: ((VirtualHost) virtualhost).getRules()) {
                farm.add(rule);
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
        farm.clear(Backend.class);
        farm.clear(BackendPool.class);
        farm.clear(Rule.class);
        farm.clear(VirtualHost.class);
        farm.setVersion(0);
        return this;
    }

    @Override
    public EntityController change(JsonObject json) throws Exception {
        final Farm farmChanged = (Farm) json.instanceOf(Farm.class);

        for (final Entity backendPool: farmChanged.getCollection(BackendPool.class)) {
            farm.add(backendPool);
            for (final Backend backend: ((BackendPool) backendPool).getBackends()) {
                farm.add(backend);
            }
        }

        for (final Entity virtualhost: farmChanged.getCollection(VirtualHost.class)) {
            farm.add(virtualhost);
            for (final Rule rule: ((VirtualHost) virtualhost).getRules()) {
                farm.add(rule);
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
