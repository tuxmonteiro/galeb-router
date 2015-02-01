package com.openvraas.core.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonObject {

    private final String json;

    private static final Gson jsonParser    = new GsonBuilder().setPrettyPrinting()
                                                               .excludeFieldsWithoutExposeAnnotation()
                                                               .create();

    public static final String NULL = jsonParser.toJson(null);

    public static String toJson(Object obj) {
        return jsonParser.toJson(obj);
    }

    public static Object fromJson(final String json, final Class<?> aClass) {
        return jsonParser.fromJson(json, aClass);
    }

    public Object instanceOf(final Class<?> aClass) {
        return fromJson(json, aClass);
    }

    public JsonObject(String json) {
        this.json = json;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
               && obj.toString().equals(this.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public String toString() {
        return json;
    }

}
