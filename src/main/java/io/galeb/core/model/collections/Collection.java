package io.galeb.core.model.collections;

import io.galeb.core.json.JsonObject;
import io.galeb.core.model.Entity;

import java.util.List;
import java.util.Set;

public interface Collection<T extends Entity, R extends Entity> extends Set<Entity> {

    public List<Entity> getListByID(String entityId);

    public List<Entity> getListByJson(JsonObject json);

    public Collection<T, R> change(Entity entity);

    public Collection<T, R> defineSetOfRelatives(Collection<? extends Entity, ? extends Entity> relatives);

    public default Collection<T, R> addChild(R child) {
        return this;
    }

    public default Collection<T, R> addToParent(R parent, T child) {
        return this;
    }
}
