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
import io.galeb.core.model.Backend;
import io.galeb.core.model.Farm;

import java.util.HashMap;
import java.util.Map;

public class BackendController implements EntityController {

    private final Farm farm;

    public BackendController(final Farm farm) {
        this.farm = farm;
    }

    @Override
    public EntityController add(JsonObject json) throws Exception {
        farm.addBackend(json);
        farm.setVersion(((Backend) json.instanceOf(Backend.class)).getVersion());
        return this;
    }

    @Override
    public EntityController del(JsonObject json) throws Exception {
        farm.delBackend(json);
        farm.setVersion(((Backend) json.instanceOf(Backend.class)).getVersion());
        return this;
    }

    @Override
    public EntityController delAll() throws Exception {
        for (final Backend backend: farm.getBackends()) {
            del(JsonObject.toJsonObject(backend));
        }
        return null;
    }

    @Override
    public EntityController change(JsonObject json) throws Exception {
        final Backend backendWithChanges = (Backend) json.instanceOf(Backend.class);
        for (final Backend backendOriginal: farm.getBackends(backendWithChanges.getId())) {
            final Map<String, Object> properties = new HashMap<>();

            properties.putAll(backendOriginal.getProperties());
            properties.putAll(backendWithChanges.getProperties());
            backendOriginal.updateModifiedAt();
            backendOriginal.setProperties(properties);
            backendOriginal.updateHash();

            farm.changeBackend(JsonObject.toJsonObject(backendOriginal));
        }
        farm.setVersion(backendWithChanges.getVersion());
        return this;
    }

    @Override
    public String get(String id) {
        if (id != null && !"".equals(id)) {
            return JsonObject.toJsonString(farm.getBackends(id));
        } else {
            return JsonObject.toJsonString(farm.getBackends());
        }
    }

}
