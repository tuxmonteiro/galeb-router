package io.galeb.core.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.NonNull;

import com.google.gson.annotations.Expose;

public class Entity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The primary key */
    @Expose private int                        pk            = -1;

    /** The id. */
    @Expose private String                     id            = "";

    /** The parent id. */
    @Expose private String                     parentId      = "";

    /** The created at. */
    @Expose private final Long                 createdAt     = System.currentTimeMillis();

    /** The modified at. */
    @Expose private Long                       modifiedAt    = System.currentTimeMillis();

    /** The properties. */
    @Expose private final Map<String, Object>  properties    = new HashMap<>();

    /** The entity type. */
    @Expose private String                     entityType    = this.getClass().getSimpleName().toLowerCase();

    /** The hash. */
    @Expose private String                     hash          = "";

    public Entity updateHash() {
        hash = "0";
        return this;
    }

    public int getPk() {
        return pk;
    }

    public Entity setPk(int pk) {
        this.pk = pk;
        return this;
    }

    public String getId() {
        return id;
    }

    public final Entity setId(@NonNull String id) {
        this.id = id;
        return this;
    }

    public String getParentId() {
        return parentId;
    }

    public final Entity setParentId(@NonNull String parentId) {
        this.parentId = parentId;
        return this;
    }

    public Long getModifiedAt() {
        return modifiedAt;
    }

    public final Entity setModifiedAt(@NonNull Long modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public final Entity setProperties(@NonNull final Map<String, Object> properties) {
        this.properties.clear();
        this.properties.putAll(properties);
        return this;
    }

    public String getEntityType() {
        return entityType;
    }

    public final Entity setEntityType(@NonNull String entityType) {
        this.entityType = entityType;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public final Entity setHash(@NonNull String hash) {
        this.hash = hash;
        return this;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Entity
               && ((Entity) obj).getId().equals(this.getId());
    }

}
