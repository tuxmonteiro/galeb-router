package com.openvraas.core.controller;

import java.util.HashSet;
import java.util.Set;

import com.openvraas.core.json.JsonObject;
import com.openvraas.core.model.Farm;

public class BackendPoolController implements EntityController {

    private final Farm farm;

    private final Set<ListenerController> listeners = new HashSet<>();

    public BackendPoolController(final Farm farm) {
        this.farm = farm;
    }

    @Override
    public EntityController add(JsonObject json) {
        farm.addBackendPool(json);
        notifyListeners(json, Action.ADD);
        return this;
    }

    @Override
    public EntityController del(JsonObject json) {
        farm.delBackendPool(json);
        notifyListeners(json, Action.DEL);
        return this;
    }

    @Override
    public EntityController change(JsonObject json) {
        farm.delBackendPool(json);
        farm.addBackendPool(json);
        notifyListeners(json, Action.CHANGE);
        return this;
    }

    @Override
    public String get(String id) {
        if (id != null && !"".equals(id)) {
            return JsonObject.toJson(farm.getBackendPool(id));
        } else {
            return JsonObject.toJson(farm.getBackendPools());
        }
    }

    @Override
    public EntityController registerListenerController(
            ListenerController listenerController) {
        listeners.add(listenerController);
        return this;
    }

    @Override
    public void notifyListeners(final JsonObject json, Action action) {
        for (ListenerController listener: listeners) {
            listener.handleController(json, action);
        }
    }

}
