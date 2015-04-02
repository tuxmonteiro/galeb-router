package io.galeb.core.controller;

import io.galeb.core.json.JsonObject;

public interface ListenerController {

    public void handleController(JsonObject json, EntityController.Action action);

}
