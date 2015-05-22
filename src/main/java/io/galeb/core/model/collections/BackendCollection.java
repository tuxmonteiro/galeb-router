package io.galeb.core.model.collections;

import io.galeb.core.json.JsonObject;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Entity;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class BackendCollection extends CopyOnWriteArraySet<Backend> implements Collection<Backend, BackendPool> {

    private static final long serialVersionUID = -3583562658521567597L;

    private Set<BackendPool> backendPools;

    @Override
    public Collection<Backend, BackendPool> defineSetOfRelatives(Set<BackendPool> relatives) {
        this.backendPools = relatives;
        return this;
    }

    @Override
    public Collection<Backend, BackendPool> addToParent(BackendPool backendPool, Backend backend) {
        backendPool.addBackend(backend);
        return this;
    }

    @Override
    public List<Backend> getListByID(String entityId) {
        return stream().filter(entity -> entity.getId().equals(entityId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Backend> getListByJson(JsonObject json) {
        final Entity entity = (Entity) json.instanceOf(Entity.class);
        return getListByID(entity.getId());
    }

    @Override
    public boolean add(Backend backend) {
        final boolean result = false;
        if (!contains(backend)) {
            backendPools.stream()
                .filter(backendPool -> backendPool.getId().equals(backend.getParentId()))
                .forEach(backendPool -> addToParent(backendPool, backend));
            super.add(backend);
        }
        return result;
    }

    @Override
    public boolean remove(Object backend) {
        final String backendId = ((Entity) backend).getId();
        backendPools.stream()
            .filter(backendPool -> backendPool.containBackend(backendId))
            .forEach(backendPool -> backendPool.delBackend(backendId));
        return super.remove(backend);
    }

    @Override
    public Collection<Backend, BackendPool> change(Backend backend) {
        if (contains(backend)) {
            backendPools.stream().filter(backendPool -> backendPool.containBackend(backend.getId()))
                .forEach(backendPool -> {
                    final Backend myBackend = backendPool.getBackend(backend.getId());
                    myBackend.setProperties(backend.getProperties());
                    myBackend.updateHash();
                    myBackend.updateModifiedAt();
                });
            stream().filter(myBackend -> myBackend.equals(backend))
                .forEach(myBackend -> {
                    myBackend.setProperties(backend.getProperties());
                    myBackend.updateHash();
                    myBackend.updateModifiedAt();
                });
        }
        return this;
    }

    @Override
    public void clear() {
        stream().forEach(backend -> this.remove(backend));
    }

}
