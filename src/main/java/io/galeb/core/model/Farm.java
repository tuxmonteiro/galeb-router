package io.galeb.core.model;

import io.galeb.core.controller.EntityController;
import io.galeb.core.json.JsonObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.inject.Alternative;

import lombok.NonNull;

import com.google.gson.annotations.Expose;

@Alternative
public class Farm extends Entity {

    private static final long serialVersionUID = 1L;

    @Expose private final Set<VirtualHost> virtualHosts = new HashSet<>();

    @Expose private final Set<BackendPool> backendPools = new HashSet<>();

    private final Map<String, EntityController> entityMap = new ConcurrentHashMap<>();

    protected final Map<String, String> options = new ConcurrentHashMap<>();

    public Farm() {
        this.setEntityType(Farm.class.getSimpleName().toLowerCase());
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
        return addVirtualHost(virtualhost);
    }

    public Farm addVirtualHost(VirtualHost virtualhost) {
        virtualHosts.add(virtualhost);
        return this;
    }

    public Farm delVirtualHost(String virtualhostId) {
        final VirtualHost virtualHost = getVirtualHost(virtualhostId);
        return delVirtualHost(virtualHost);
    }

    public Farm delVirtualHost(JsonObject jsonObject) {
        final VirtualHost virtualHost = getVirtualHost(jsonObject);
        return delVirtualHost(virtualHost);
    }

    public Farm delVirtualHost(VirtualHost virtualHost) {
        if (virtualHost!=null) {
            virtualHosts.remove(virtualHost);
        }
        return this;
    }

    public boolean containVirtualHost(JsonObject jsonObject) {
        return getVirtualHost(jsonObject) != null;
    }

    public boolean containVirtualHost(String virtualhostId) {
        return getVirtualHost(virtualhostId) != null;
    }

    public void clearVirtualHosts() {
        virtualHosts.clear();
    }

    public Farm addBackendPool(JsonObject jsonObject) {
        final BackendPool backendPool = (BackendPool) JsonObject.fromJson(jsonObject.toString(), BackendPool.class);
        return addBackendPool(backendPool);
    }

    public Farm addBackendPool(BackendPool backendPool) {
        backendPools.add(backendPool);
        return this;
    }

    public Farm delBackendPool(JsonObject jsonObject) {
        final BackendPool backendPool = getBackendPool(jsonObject);
        return delBackendPool(backendPool);
    }

    public Farm delBackendPool(String backendId) {
        final BackendPool backendPool = getBackendPool(backendId);
        return delBackendPool(backendPool);
    }

    public Farm delBackendPool(BackendPool backendPool) {
        if (backendPool!=null) {
            backendPools.remove(backendPool);
        }
        return this;
    }

    public boolean containBackendPool(JsonObject jsonObject) {
        return getBackendPool(jsonObject) != null;
    }

    public boolean containBackendPool(String backendPoolId) {
        return getBackendPool(backendPoolId) != null;
    }

    public void clearBackendPool() {
        backendPools.clear();
    }

    public Farm addBackend(JsonObject jsonObject) {
        final Backend backend = (Backend) JsonObject.fromJson(jsonObject.toString(), Backend.class);
        return addBackend(backend);
    }

    public Farm addBackend(@NonNull Backend backend) {
        final BackendPool backendPool = getBackendPool(backend.getParentId());
        if (backendPool!=null) {
            backendPool.addBackend(backend);
        }
        return this;
    }

    public Farm delBackend(String backendId) {
        for (final BackendPool backendPool: backendPools) {
            final Backend backend = backendPool.getBackend(backendId);
            if (backend!=null) {
                delBackend(backend);
            }
        }
        return this;
    }

    public Farm delBackend(JsonObject jsonObject) {
        final Backend backend = (Backend) JsonObject.fromJson(jsonObject.toString(), Backend.class);
        return delBackend(backend);
    }

    public Farm delBackend(@NonNull Backend backend) {
        final BackendPool backendPool = getBackendPool(backend.getParentId());
        if (backendPool!=null) {
            backendPool.delBackend(backend);
        }
        return this;
    }

    public List<Backend> getBackend(String backendId) {
        final List<Backend> backends = new ArrayList<>();
        for (final BackendPool backendPool: backendPools) {
            final Backend backend = backendPool.getBackend(backendId);
            if (backend!=null) {
                backends.add(backend);
            }
        }
        return backends.isEmpty() ? null : backends;
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
        return addRule(rule);
    }

    public Farm addRule(Rule rule) {
        final VirtualHost virtualHost = getVirtualHost(rule.getParentId());
        if (virtualHost!=null) {
            virtualHost.addRule(rule);
        }
        return this;
    }

    public Farm delRule(Rule rule) {
        return delRule(rule.getId());
    }

    public Farm delRule(JsonObject jsonObject) {
        final Rule rule = (Rule) JsonObject.fromJson(jsonObject.toString(), Rule.class);
        return delRule(rule.getId());
    }

    public Farm delRule(String ruleId) {
        for (final VirtualHost virtualHost: virtualHosts) {
            final Rule rule = virtualHost.getRule(ruleId);
            if (rule!=null) {
                getVirtualHost(virtualHost.getId()).delRule(ruleId);
            }
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
        return rules.isEmpty() ? null : rules;
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
