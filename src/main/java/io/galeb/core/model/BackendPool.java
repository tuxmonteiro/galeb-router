/*
 * Copyright (c) 2014-2015 Globo.com - ATeam
 * All rights reserved.
 *
 * This source is subject to the Apache License, Version 2.0.
 * Please see the LICENSE file for more information.
 *
 * Authors: See AUTHORS file
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.galeb.core.model;

import io.galeb.core.json.JsonObject;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.google.gson.annotations.Expose;

public class BackendPool extends Entity {

    private static final long serialVersionUID = 1L;

    private static final String PROP_BACKEND_WITH_LEASTCONN = "backendWithLeastConn";

    public static final String PROP_HEALTHCHECK_RETURN      = "hcExpectedReturn";

    public static final String PROP_HEALTHCHECK_PATH        = "hcPath";

    public static final String PROP_HEALTHCHECK_HOST        = "hcHost";

    public static final String PROP_LOADBALANCE_POLICY      = "loadBalancePolicy";

    @Expose private final Set<Backend> backends = new CopyOnWriteArraySet<>();

    public Backend getBackendWithLeastConn() {
        if (!getBackends().isEmpty()) {
            final String backendID = (String) getProperty(PROP_BACKEND_WITH_LEASTCONN);
            if (backendID!=null) {
                return getBackend(backendID);
            }
        }
        return null;
    }

    public synchronized void setBackendWithLeastConn(final Backend backendWithLeastConnObj) {
        putProperty(PROP_BACKEND_WITH_LEASTCONN, backendWithLeastConnObj.getId());
    }

    public BackendPool() {
        super();
    }

    public BackendPool(BackendPool backendPool) {
        this();
        setPk(backendPool.getPk());
        setId(backendPool.getId());
        setParentId(backendPool.getParentId());
        setProperties(backendPool.getProperties());
        setBackends(backendPool.getBackends());
        updateHash();
    }

    public Backend getBackend(String backendId) {
        Backend backend = null;
        for (final Backend backendTemp : backends) {
            if (backendId.equals(backendTemp.getId())) {
                backend = backendTemp;
                break;
            }
        }
        return backend;
    }

    public BackendPool addBackend(String json) {
        final Backend backend = (Backend) JsonObject.fromJson(json, Backend.class);
        return addBackend(backend);
    }

    public BackendPool addBackend(Backend backend) {
        backends.add(backend);
        return this;
    }

    public BackendPool delBackend(String backendId) {
        final Backend backend = getBackend(backendId);
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

    public void setBackends(final Set<Backend> myBackends) {
        final Set<Backend> copyBackends = new HashSet<>(myBackends);
        backends.clear();
        backends.addAll(copyBackends);
    }

}
