package io.galeb.core.controller;

import io.galeb.core.json.JsonObject;
import io.galeb.core.model.Farm;

import java.util.HashSet;
import java.util.Set;

public class VirtualHostController implements EntityController {

    private final Farm farm;

    private final Set<ListenerController> listeners = new HashSet<>();

    public VirtualHostController(final Farm farm) {
        this.farm = farm;
    }

    @Override
    public EntityController add(JsonObject json) {
        farm.addVirtualHost(json);
        notifyListeners(json, Action.ADD);
        return this;
    }

    @Override
    public EntityController del(JsonObject json) {
        farm.delVirtualHost(json);
        notifyListeners(json, Action.DEL);
        return this;
    }

    @Override
    public EntityController change(JsonObject json) {
        farm.delVirtualHost(json);
        farm.addVirtualHost(json);
        notifyListeners(json, Action.CHANGE);
        return this;
    }

    @Override
    public String get(String id) {
        if (id != null && !"".equals(id)) {
            return JsonObject.toJson(farm.getVirtualHost(id));
        } else {
            return JsonObject.toJson(farm.getVirtualHosts());
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
