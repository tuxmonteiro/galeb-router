package io.galeb.core.model.collections;

import io.galeb.core.json.JsonObject;
import io.galeb.core.loadbalance.LoadBalancePolicy;
import io.galeb.core.loadbalance.LoadBalancePolicyLocator;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class BackendPoolCollection implements Collection<BackendPool, Backend> {

    private Set<Entity> backendPools = new CopyOnWriteArraySet<>();

    private Collection<? extends Entity, ? extends Entity> backends;

    @Override
    public Collection<BackendPool, Backend> defineSetOfRelatives(Collection<? extends Entity, ? extends Entity> relatives) {
        backends = relatives;
        return this;
    }

    @Override
    public Collection<BackendPool, Backend> addChild(Backend backend) {
        backends.add(backend);
        return this;
    }

    @Override
    public List<Entity> getListByID(String entityId) {
        return backendPools.stream().filter(entity -> entity.getId().equals(entityId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Entity> getListByJson(JsonObject json) {
        final Entity entity = (Entity) json.instanceOf(Entity.class);
        return getListByID(entity.getId());
    }

    @Override
    public boolean add(Entity backendPool) {
        final boolean result = false;
        if (!contains(backendPool)) {
            backends.stream()
                .filter(backend -> backend.getParentId().equals(backendPool.getId()))
                .forEach(backend -> addChild((Backend) backend));
            backendPool.setProperties(defineLoadBalancePolicy((BackendPool) backendPool));
            backendPools.add(backendPool);
        }
        return result;
    }

    @Override
    public boolean remove(Object o) {
        return backendPools.remove(o);
    }

    @Override
    public Collection<BackendPool, Backend> change(Entity backendPool) {
        if (contains(backendPool)) {
            remove(backendPool);
            add(backendPool);
        }
        return this;
    }

    private Map<String, Object> defineLoadBalancePolicy(final BackendPool backendPool) {
        final Map<String, Object> properties = new HashMap<>(backendPool.getProperties());
        final String loadBalanceAlgorithm = (String) properties.get(BackendPool.PROP_LOADBALANCE_POLICY);
        final boolean loadBalanceDefined = LoadBalancePolicy.hasLoadBalanceAlgorithm(loadBalanceAlgorithm);

        if (!loadBalanceDefined) {
            properties.put(BackendPool.PROP_LOADBALANCE_POLICY, LoadBalancePolicyLocator.DEFAULT_ALGORITHM.toString());
        }
        return properties;
    }

    @Override
    public void clear() {
        backendPools.stream().forEach(backendPool -> remove(backendPool));
    }

    @Override
    public int size() {
        return backendPools.size();
    }

    @Override
    public boolean isEmpty() {
        return backendPools.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return backendPools.contains(o);
    }

    @Override
    public Iterator<Entity> iterator() {
        return backendPools.iterator();
    }

    @Override
    public Object[] toArray() {
        return backendPools.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return backendPools.toArray(a);
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        return backendPools.containsAll(c);
    }

    @Override
    public boolean addAll(java.util.Collection<? extends Entity> c) {
        return backendPools.addAll(c);
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        return backendPools.retainAll(c);
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        return backendPools.removeAll(c);
    }

}
