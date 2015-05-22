package io.galeb.core.model.collections;

import io.galeb.core.json.JsonObject;
import io.galeb.core.loadbalance.LoadBalancePolicy;
import io.galeb.core.loadbalance.LoadBalancePolicyLocator;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class BackendPoolCollection extends CopyOnWriteArraySet<BackendPool> implements Collection<BackendPool, Backend> {

    private static final long serialVersionUID = -2273339359312055504L;

    private Set<Backend> backends;

    @Override
    public Collection<BackendPool, Backend> defineSetOfRelatives(Set<Backend> relatives) {
        this.backends = relatives;
        return this;
    }

    @Override
    public Collection<BackendPool, Backend> addChild(Backend backend) {
        backends.add(backend);
        return this;
    }

    @Override
    public List<BackendPool> getListByID(String entityId) {
        return stream().filter(entity -> entity.getId().equals(entityId))
                .collect(Collectors.toList());
    }

    @Override
    public List<BackendPool> getListByJson(JsonObject json) {
        final Entity entity = (Entity) json.instanceOf(Entity.class);
        return getListByID(entity.getId());
    }

    @Override
    public boolean add(BackendPool backendPool) {
        final boolean result = false;
        if (!contains(backendPool)) {
            backends.stream()
                .filter(backend -> backend.getParentId().equals(backendPool.getId()))
                .forEach(backend -> addChild(backend));
            backendPool.setProperties(defineLoadBalancePolicy(backendPool));
            super.add(backendPool);
        }
        return result;
    }

    @Override
    public Collection<BackendPool, Backend> change(BackendPool backendPool) {
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

}
