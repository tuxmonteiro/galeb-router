package io.galeb.core.model;

import io.galeb.core.json.JsonObject;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.annotations.Expose;

public class BackendPool extends Entity {

    private static final long serialVersionUID = 1L;

    private static final String PROP_BACKEND_WITH_LEASTCONN = "backendWithLeastConn";

    @Expose private Set<Backend> backends = new HashSet<>();

    public Backend getBackendWithLeastConn() {
        String backendID = (String) getProperties().get(PROP_BACKEND_WITH_LEASTCONN);
        return backendID != null ? getBackend(backendID) : (Backend) getBackends().toArray()[0];
    }

    public synchronized void setBackendWithLeastConn(final Backend backendWithLeastConnObj) {
        getProperties().put(PROP_BACKEND_WITH_LEASTCONN, backendWithLeastConnObj.getId());
    }

    public Backend getBackend(String backendId) {
        Backend backend = null;
        for (Backend backendTemp : backends) {
            if (backendId.equals(backendTemp.getId())) {
                backend = backendTemp;
                break;
            }
        }
        return backend;
    }

    public BackendPool addBackend(String json) {
        Backend backend = (Backend) JsonObject.fromJson(json, Backend.class);
        return addBackend(backend);
    }

    public BackendPool addBackend(Backend backend) {
        backends.add(backend);
        return this;
    }

    public BackendPool delBackend(String backendId) {
        Backend backend = getBackend(backendId);
        return delBackend(backend);
    }

    public BackendPool delBackend(Backend backend) {
        if (backend!=null) {
            backends.remove(backend);
        }
        return this;
    }

    public boolean containBackend(String backendId) {
        return getBackend(backendId) != null;
    }

    public void clearBackends() {
        backends.clear();
    }

    public Set<Backend> getBackends() {
        return backends;
    }

}
