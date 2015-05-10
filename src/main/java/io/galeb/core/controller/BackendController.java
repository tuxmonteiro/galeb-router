package io.galeb.core.controller;

import io.galeb.core.json.JsonObject;
import io.galeb.core.model.Backend;
import io.galeb.core.model.Farm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BackendController implements EntityController {

    private final Farm farm;

    private final Set<ListenerController> listeners = new HashSet<>();

    public BackendController(final Farm farm) {
        this.farm = farm;
    }

    @Override
    public EntityController add(JsonObject json) throws Exception {
        farm.addBackend(json);
        notifyListeners(json, Action.ADD);
        return this;
    }

    @Override
    public EntityController del(JsonObject json) throws Exception {
        farm.delBackend(json);
        notifyListeners(json, Action.DEL);
        return this;
    }

    @Override
    public EntityController change(JsonObject json) throws Exception {
        Backend backendWithChanges = (Backend) JsonObject.fromJson(json.toString(), Backend.class);
        for (Backend backendOriginal: farm.getBackends(backendWithChanges.getId())) {
            Map<String, Object> properties = new HashMap<>();

            properties.putAll(backendOriginal.getProperties());
            properties.putAll(backendWithChanges.getProperties());
            backendOriginal.setModifiedAt(System.currentTimeMillis());
            backendOriginal.setProperties(properties);
            backendOriginal.updateHash();

            farm.changeBackend(JsonObject.toJsonObject(backendOriginal));
            notifyListeners(json, Action.CHANGE);
        }
        return this;
    }

    @Override
    public String get(String id) {
        if (id != null && !"".equals(id)) {
            return JsonObject.toJsonString(farm.getBackends(id));
        } else {
            return JsonObject.toJsonString(farm.getBackends());
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
        for (final ListenerController listener: listeners) {
            listener.handleController(json, action);
        }
    }

}
