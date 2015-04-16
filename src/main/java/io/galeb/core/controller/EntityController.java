package io.galeb.core.controller;

import io.galeb.core.json.JsonObject;

public interface EntityController {

    public enum Action {
        ADD,
        DEL,
        CHANGE
    }

    public static final EntityController NULL = new EntityController() {
        @Override
        public EntityController registerListenerController(
                ListenerController listenerController) {
            return this;
        }

        @Override
        public void notifyListeners(JsonObject json, Action action) {
            return;
        }

        @Override
        public String get(String id) {
            return "NULL";
        }

        @Override
        public EntityController del(JsonObject json) {
            return this;
        }

        @Override
        public EntityController change(JsonObject json) {
            return this;
        }

        @Override
        public EntityController add(JsonObject json) {
            return this;
        }
    };

    public EntityController add(JsonObject json) throws Exception;

    public EntityController del(JsonObject json) throws Exception;

    public EntityController change(JsonObject json) throws Exception;

    public String get(String id);

    public EntityController registerListenerController(final ListenerController listenerController);

    public void notifyListeners(final JsonObject json, Action action);

}
