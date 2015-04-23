package io.galeb.core.eventbus;

import io.galeb.core.json.JsonObject;
import io.galeb.core.model.Entity;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Expose
    private final Object type;

    @Expose
    private final Entity data;

    public Event(Object type, Entity data) {
        this.type = type;
        this.data = data;
    }

    public Object getType() {
        return type;
    }

    public JsonObject getData() {
        return JsonObject.toJsonObject(data);
    }

}
