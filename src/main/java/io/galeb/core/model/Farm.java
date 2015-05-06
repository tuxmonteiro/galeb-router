package io.galeb.core.model;

import io.galeb.core.controller.EntityController;
import io.galeb.core.json.JsonObject;
import io.galeb.core.loadbalance.LoadBalancePolicy;
import io.galeb.core.loadbalance.LoadBalancePolicyLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.inject.Alternative;

import com.google.gson.annotations.Expose;

@Alternative
public class Farm extends Entity {

    private static final long serialVersionUID = 1L;

    @Expose private final Set<VirtualHost> virtualHosts = new HashSet<>();

    @Expose private final Set<BackendPool> backendPools = new HashSet<>();

    private final Map<String, EntityController> entityMap = new ConcurrentHashMap<>();

    protected final Map<String, String> options = new ConcurrentHashMap<>();

    public Farm() {
        setEntityType(Farm.class.getSimpleName().toLowerCase());
    }

    public Farm setOptions(Map<String, String> options) {
        this.options.putAll(options);
        return this;
    }

    public Map<String, EntityController> getEntityMap() {
        return entityMap;
    }

    public VirtualHost getVirtualHost(String virtualHostId) {
        VirtualHost virtualHost = null;
        for (final VirtualHost virtualHostTemp : virtualHosts) {
            if (virtualHostTemp.getId().equals(virtualHostId)) {
                virtualHost = virtualHostTemp;
                break;
            }
        }
        return virtualHost;
    }

    public VirtualHost getVirtualHost(JsonObject jsonObject) {
        final String virtualHostId = ((VirtualHost) JsonObject.fromJson(jsonObject.toString(), VirtualHost.class)).getId();
        return getVirtualHost(virtualHostId);
    }

    public BackendPool getBackendPool(String backendPoolId) {

        BackendPool backendPool = null;
        for (final BackendPool backendPoolTemp : backendPools) {
            if (backendPoolId.equals(backendPoolTemp.getId())) {
                backendPool = backendPoolTemp;
                break;
            }
        }
        return backendPool;
    }

    public BackendPool getBackendPool(JsonObject jsonObject) {
        final String backendPoolId = ((BackendPool) JsonObject.fromJson(jsonObject.toString(), BackendPool.class)).getId();
        return getBackendPool(backendPoolId);
    }

    public Set<VirtualHost> getVirtualHosts() {
        return virtualHosts;
    }

    public Set<BackendPool> getBackendPools() {
        return backendPools;
    }

    public Farm addVirtualHost(JsonObject jsonObject) {
        final VirtualHost virtualhost = (VirtualHost) JsonObject.fromJson(jsonObject.toString(), VirtualHost.class);
        virtualHosts.add(virtualhost);
        return this;
    }

    public Farm addVirtualHost(VirtualHost virtualhost) {
        return addVirtualHost(JsonObject.toJsonObject(virtualhost));
    }

    public Farm changeVirtualHost(JsonObject jsonObject) {
        if (containVirtualHost(jsonObject)) {
            delVirtualHost(jsonObject);
            addVirtualHost(jsonObject);
        }
        return this;
    }

    public Farm changeVirtualHost(VirtualHost virtualHost) {
        return changeBackendPool(JsonObject.toJsonObject(virtualHost));
    }

    public Farm delVirtualHost(String virtualhostId) {
        final VirtualHost virtualHost = getVirtualHost(virtualhostId);
        return delVirtualHost(virtualHost);
    }

    public Farm delVirtualHost(JsonObject jsonObject) {
        final VirtualHost virtualHost = getVirtualHost(jsonObject);
        if (virtualHost!=null) {
            virtualHosts.remove(virtualHost);
        }
        return this;
    }

    public Farm delVirtualHost(VirtualHost virtualHost) {
        return delVirtualHost(JsonObject.toJsonObject(virtualHost));
    }

    public boolean containVirtualHost(JsonObject jsonObject) {
        return getVirtualHost(jsonObject) != null;
    }

    public boolean containVirtualHost(String virtualhostId) {
        return getVirtualHost(virtualhostId) != null;
    }

    public void clearVirtualHosts() {
        for (VirtualHost virtualHost: getVirtualHosts()) {
            delVirtualHost(JsonObject.toJsonObject(virtualHost));
        }
    }

    private Map<String, Object> defineLoadBalancePolicy(final BackendPool backendPool) {
        final Map<String, Object> properties = new HashMap<>(backendPool.getProperties());
        final String loadBalanceAlgorithm = (String) properties.get(LoadBalancePolicy.LOADBALANCE_POLICY_FIELD);
        final boolean loadBalanceDefined = loadBalanceAlgorithm!=null && LoadBalancePolicy.hasLoadBalanceAlgorithm(loadBalanceAlgorithm);

        if (!loadBalanceDefined) {
            properties.put(LoadBalancePolicy.LOADBALANCE_POLICY_FIELD, LoadBalancePolicyLocator.DEFAULT_ALGORITHM.toString());
        }
        return properties;
    }

    public Farm addBackendPool(JsonObject jsonObject) {
        final BackendPool backendPool = (BackendPool) JsonObject.fromJson(jsonObject.toString(), BackendPool.class);
        backendPool.setProperties(defineLoadBalancePolicy(backendPool));
        backendPools.add(backendPool);
        return this;
    }

    public Farm addBackendPool(BackendPool backendPool) {
        return addBackendPool(JsonObject.toJsonObject(backendPool));
    }

    public Farm changeBackendPool(JsonObject jsonObject) {
        if (containBackendPool(jsonObject)) {
            delBackendPool(jsonObject);
            addBackendPool(jsonObject);
        }
        return this;
    }

    public Farm changeBackendPool(BackendPool backendPool) {
        return changeBackendPool(JsonObject.toJsonObject(backendPool));
    }

    public Farm delBackendPool(JsonObject jsonObject) {
        final BackendPool backendPool = getBackendPool(jsonObject);
        if (backendPool!=null) {
            backendPools.remove(backendPool);
        }
        return this;
    }

    public Farm delBackendPool(String backendId) {
        final BackendPool backendPool = getBackendPool(backendId);
        return delBackendPool(backendPool);
    }

    public Farm delBackendPool(BackendPool backendPool) {
        return delBackendPool(JsonObject.toJsonObject(backendPool));
    }

    public boolean containBackendPool(JsonObject jsonObject) {
        return getBackendPool(jsonObject) != null;
    }

    public boolean containBackendPool(String backendPoolId) {
        return getBackendPool(backendPoolId) != null;
    }

    public void clearBackendPool() {
        for (BackendPool backendPool: getBackendPools()) {
            delBackendPool(JsonObject.toJsonObject(backendPool));
        }
    }

    public Farm addBackend(JsonObject jsonObject) {
        final Backend backend = (Backend) JsonObject.fromJson(jsonObject.toString(), Backend.class);
        final BackendPool backendPool = getBackendPool(backend.getParentId());
        if (backendPool!=null) {
            backendPool.addBackend(backend);
        }
        return this;
    }

    public Farm addBackend(Backend backend) {
        return addBackend(JsonObject.toJsonObject(backend));
    }

    public Farm changeBackend(JsonObject jsonObject) {
        final Backend backend = (Backend) JsonObject.fromJson(jsonObject.toString(), Backend.class);
        final BackendPool backendPool = getBackendPool(backend.getParentId());
        if (backendPool != null && backendPool.containBackend(backend.getId())) {
            delBackend(jsonObject);
            addBackend(jsonObject);
        }
        return this;
    }

    public Farm changeBackend(Backend backend) {
        return changeBackend(JsonObject.toJsonObject(backend));
    }

    public Farm delBackend(JsonObject jsonObject) {
        final Backend backend = (Backend) JsonObject.fromJson(jsonObject.toString(), Backend.class);
        final BackendPool backendPool = getBackendPool(backend.getParentId());
        if (backendPool!=null) {
            backendPool.delBackend(backend);
        }
        return this;
    }

    public Farm delBackend(Backend backend) {
        return delBackend(JsonObject.toJsonObject(backend));
    }

    public List<Backend> getBackend(String backendId) {
        final List<Backend> backends = new ArrayList<>();
        for (final BackendPool backendPool: backendPools) {
            final Backend backend = backendPool.getBackend(backendId);
            if (backend!=null) {
                backends.add(backend);
            }
        }
        return backends;
    }

    public List<Backend> getBackends() {
        final List<Backend> backends = new ArrayList<>();
        for (final BackendPool backendPool: backendPools) {
            backends.addAll(backendPool.getBackends());
        }
        return backends;
    }

    public Farm addRule(JsonObject jsonObject) {
        final Rule rule = (Rule) JsonObject.fromJson(jsonObject.toString(), Rule.class);
        final VirtualHost virtualHost = getVirtualHost(rule.getParentId());
        if (virtualHost!=null) {
            virtualHost.addRule(rule);
        }
        return this;
    }

    public Farm addRule(Rule rule) {
        return addRule(JsonObject.toJsonObject(rule));
    }

    public Farm delRule(Rule rule) {
        return delRule(JsonObject.toJsonObject(rule));
    }

    public Farm delRule(JsonObject jsonObject) {
        final Rule rule = (Rule) JsonObject.fromJson(jsonObject.toString(), Rule.class);
        final VirtualHost virtualHost = getVirtualHost(rule.getParentId());
        if (virtualHost != null) {
            virtualHost.delRule(rule.getId());
        }
        return this;
    }

    public List<Rule> getRule(String ruleId) {
        final List<Rule> rules = new ArrayList<>();
        for (final VirtualHost virtualHost: virtualHosts) {
            final Rule rule = virtualHost.getRule(ruleId);
            if (rule!=null) {
                rules.add(rule);
            }
        }
        return rules;
    }

    public List<Rule> getRules() {
        final List<Rule> rules = new ArrayList<>();
        for (final VirtualHost virtualHost: virtualHosts) {
            rules.addAll(virtualHost.getRules());
        }
        return rules;
    }

    public Object getRootHandler() {
        return null;
    }
}
