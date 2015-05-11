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

package io.galeb.core.controller;

import io.galeb.core.json.JsonObject;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Farm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BackendPoolController implements EntityController {

    private final Farm farm;

    private final Set<ListenerController> listeners = new HashSet<>();

    public BackendPoolController(final Farm farm) {
        this.farm = farm;
    }

    @Override
    public EntityController add(JsonObject json) throws Exception {
        farm.addBackendPool(json);
        notifyListeners(json, Action.ADD);
        return this;
    }

    @Override
    public EntityController del(JsonObject json) throws Exception {
        farm.delBackendPool(json);
        notifyListeners(json, Action.DEL);
        return this;
    }

    @Override
    public EntityController change(JsonObject json) throws Exception {
        BackendPool backendPoolWithChanges = (BackendPool) JsonObject.fromJson(json.toString(), BackendPool.class);
        BackendPool backendPoolOriginal = farm.getBackendPool(json);
        if (backendPoolOriginal!=null) {
            Map<String, Object> properties = new HashMap<>();

            properties.putAll(backendPoolOriginal.getProperties());
            properties.putAll(backendPoolWithChanges.getProperties());
            backendPoolOriginal.setModifiedAt(System.currentTimeMillis());
            backendPoolOriginal.setProperties(properties);
            backendPoolOriginal.updateHash();

            farm.changeBackendPool(JsonObject.toJsonObject(backendPoolOriginal));
            notifyListeners(json, Action.CHANGE);
        }
        return this;
    }

    @Override
    public String get(String id) {
        if (id != null && !"".equals(id)) {
            return JsonObject.toJsonString(farm.getBackendPool(id));
        } else {
            return JsonObject.toJsonString(farm.getBackendPools());
        }
    }

    @Override
    public EntityController registerListenerController(
            ListenerController listenerController) {
        listeners.add(listenerController);
        return this;
    }

    @Override
    public void notifyListeners(final JsonObject json, Action action) {
        for (final ListenerController listener: listeners) {
            listener.handleController(json, action);
        }
    }

}
