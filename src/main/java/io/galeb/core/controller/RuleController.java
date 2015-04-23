package io.galeb.core.controller;

import io.galeb.core.json.JsonObject;
import io.galeb.core.model.Farm;

import java.util.HashSet;
import java.util.Set;

public class RuleController implements EntityController {

    private final Farm farm;

    private final Set<ListenerController> listeners = new HashSet<>();

    public RuleController(final Farm farm) {
        this.farm = farm;
    }

    @Override
    public EntityController add(JsonObject json) throws Exception {
        farm.addRule(json);
        notifyListeners(json, Action.ADD);
        return this;
    }

    @Override
    public EntityController del(JsonObject json) throws Exception {
        farm.delRule(json);
        notifyListeners(json, Action.DEL);
        return this;
    }

    @Override
    public EntityController change(JsonObject json) throws Exception {
        farm.delRule(json);
        farm.addRule(json);
        notifyListeners(json, Action.CHANGE);
        return this;
    }

    @Override
    public String get(String id) {
        if (id != null && !"".equals(id)) {
            return JsonObject.toJsonString(farm.getRule(id));
        } else {
            return JsonObject.toJsonString(farm.getRules());
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
