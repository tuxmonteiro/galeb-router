package com.openvraas.core.controller;

import com.openvraas.core.json.JsonObject;

public interface EntityController {

    public enum Action {
        ADD,
        DEL,
        CHANGE
    }

    public EntityController add(JsonObject json);

    public EntityController del(JsonObject json);

    public EntityController change(JsonObject json);

    public String get(String id);

    public EntityController registerListenerController(ListenerController listenerController);

    public void notifyListeners(final JsonObject json, Action action);

}
