package com.openvraas.core.controller;

import com.openvraas.core.json.JsonObject;

public interface ListenerController {

    public void handleController(JsonObject json, EntityController.Action action);

}
