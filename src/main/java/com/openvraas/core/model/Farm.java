package com.openvraas.core.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.inject.Alternative;

import com.google.gson.annotations.Expose;
import com.openvraas.core.controller.EntityController;
import com.openvraas.core.json.JsonObject;

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
        for (VirtualHost virtualHostTemp : virtualHosts) {
            if (virtualHostTemp.getId().equals(virtualHostId)) {
                virtualHost = virtualHostTemp;
                break;
            }
        }
        return virtualHost;
    }

    public VirtualHost getVirtualHost(JsonObject jsonObject) {
        String virtualHostId = ((VirtualHost) JsonObject.fromJson(jsonObject.toString(), VirtualHost.class)).getId();
        return getVirtualHost(virtualHostId);
    }

    public BackendPool getBackendPool(String backendPoolId) {

        BackendPool backendPool = null;
        for (BackendPool backendPoolTemp : backendPools) {
            if (backendPoolId.equals(backendPoolTemp.getId())) {
                backendPool = backendPoolTemp;
                break;
            }
        }
        return backendPool;
    }

    public BackendPool getBackendPool(JsonObject jsonObject) {
        String backendPoolId = ((BackendPool) JsonObject.fromJson(jsonObject.toString(), BackendPool.class)).getId();
        return getBackendPool(backendPoolId);
    }

    public Set<VirtualHost> getVirtualHosts() {
        return virtualHosts;
    }

    public Set<BackendPool> getBackendPools() {
        return backendPools;
    }

    public Farm addVirtualHost(JsonObject jsonObject) {
        VirtualHost virtualhost = (VirtualHost) JsonObject.fromJson(jsonObject.toString(), VirtualHost.class);
        return addVirtualHost(virtualhost);
    }

    public Farm addVirtualHost(VirtualHost virtualhost) {
        virtualHosts.add(virtualhost);
        return this;
    }

    public Farm delVirtualHost(String virtualhostId) {
        VirtualHost virtualHost = getVirtualHost(virtualhostId);
        return delVirtualHost(virtualHost);
    }

    public Farm delVirtualHost(JsonObject jsonObject) {
        VirtualHost virtualHost = getVirtualHost(jsonObject);
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
        BackendPool backendPool = (BackendPool) JsonObject.fromJson(jsonObject.toString(), BackendPool.class);
        return addBackendPool(backendPool);
    }

    public Farm addBackendPool(BackendPool backendPool) {
        backendPools.add(backendPool);
        return this;
    }

    public Farm delBackendPool(JsonObject jsonObject) {
        BackendPool backendPool = getBackendPool(jsonObject);
        return delBackendPool(backendPool);
    }

    public Farm delBackendPool(String backendId) {
        BackendPool backendPool = getBackendPool(backendId);
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

    public Farm addBackend(JsonObject jsonObject) throws Exception {
        Backend backend = (Backend) JsonObject.fromJson(jsonObject.toString(), Backend.class);
        return addBackend(backend);
    }

    public Farm addBackend(Backend backend) {
        if (backend!=null) {
            BackendPool backendPool = getBackendPool(backend.getParentId());
            if (backendPool!=null) {
                backendPool.addBackend(backend);
            }
        }
        return this;
    }

    public Farm delBackend(String backendId) {
        for (BackendPool backendPool: backendPools) {
            Backend backend = backendPool.getBackend(backendId);
            if (backend!=null) {
                delBackend(backend);
            }
        }
        return this;
    }

    public Farm delBackend(JsonObject jsonObject) throws Exception {
        Backend backend = (Backend) JsonObject.fromJson(jsonObject.toString(), Backend.class);
        return delBackend(backend);
    }

    public Farm delBackend(Backend backend) {
        if (backend!=null) {
            BackendPool backendPool = getBackendPool(backend.getParentId());
            if (backendPool!=null) {
                backendPool.delBackend(backend);
            }
        }
        return this;
    }

    public List<Backend> getBackend(String backendId) {
        List<Backend> backends = new ArrayList<>();
        for (BackendPool backendPool: backendPools) {
            Backend backend = backendPool.getBackend(backendId);
            if (backend!=null) {
                backends.add(backend);
            }
        }
        return backends.isEmpty() ? null : backends;
    }

    public List<Backend> getBackends() {
        List<Backend> backends = new ArrayList<>();
        for (BackendPool backendPool: backendPools) {
            backends.addAll(backendPool.getBackends());
        }
        return backends;
    }

    public Farm addRule(JsonObject jsonObject) {
        Rule rule = (Rule) JsonObject.fromJson(jsonObject.toString(), Rule.class);
        return addRule(rule);
    }

    public Farm addRule(Rule rule) {
        VirtualHost virtualHost = getVirtualHost(rule.getParentId());
        if (virtualHost!=null) {
            virtualHost.addRule(rule);
        }
        return this;
    }

    public Farm delRule(Rule rule) {
        return delRule(rule.getId());
    }

    public Farm delRule(JsonObject jsonObject) {
        Rule rule = (Rule) JsonObject.fromJson(jsonObject.toString(), Rule.class);
        return delRule(rule.getId());
    }

    public Farm delRule(String ruleId) {
        for (VirtualHost virtualHost: virtualHosts) {
            Rule rule = virtualHost.getRule(ruleId);
            if (rule!=null) {
                getVirtualHost(virtualHost.getId()).delRule(ruleId);
            }
        }
        return this;
    }

    public List<Rule> getRule(String ruleId) {
        List<Rule> rules = new ArrayList<>();
        for (VirtualHost virtualHost: virtualHosts) {
            Rule rule = virtualHost.getRule(ruleId);
            if (rule!=null) {
                rules.add(rule);
            }
        }
        return rules.isEmpty() ? null : rules;
    }

    public List<Rule> getRules() {
        List<Rule> rules = new ArrayList<>();
        for (VirtualHost virtualHost: virtualHosts) {
            rules.addAll(virtualHost.getRules());
        }
        return rules;
    }

    public Object getRootHandler() {
        return null;
    }
}
