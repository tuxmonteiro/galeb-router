package io.galeb.core.model.collections;

import io.galeb.core.json.JsonObject;

import java.util.List;
import java.util.Set;

public interface Collection<T, R> {

    public List<T> getListByID(String entityId);

    public List<T> getListByJson(JsonObject json);

    public Collection<T, R> change(T entity);

    public Collection<T, R> defineSetOfRelatives(Set<R> relatives);

    public default Collection<T, R> addChild(R child) {
        return this;
    }

    public default Collection<T, R> addToParent(R parent, T child) {
        return this;
    }
}
