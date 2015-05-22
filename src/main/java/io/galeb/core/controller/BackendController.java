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
import io.galeb.core.model.collections.BackendCollection;

public class BackendController implements EntityController {

    private final Farm farm;

    private final BackendCollection backendCollection;

    public BackendController(final Farm farm) {
        this.farm = farm;
        this.backendCollection = (BackendCollection) farm.getBackends();
    }

    @Override
    public EntityController add(JsonObject json) throws Exception {
        final Backend backend = (Backend) json.instanceOf(Backend.class);
        backendCollection.add(backend);
        farm.setVersion((backend).getVersion());
        return this;
    }

    @Override
    public EntityController del(JsonObject json) throws Exception {
        final Backend backend = (Backend) json.instanceOf(Backend.class);
        backendCollection.remove(backend);
        farm.setVersion((backend).getVersion());
        return this;
    }

    @Override
    public EntityController delAll() throws Exception {
        backendCollection.clear();
        return null;
    }

    @Override
    public EntityController change(JsonObject json) throws Exception {
        final Backend backendWithChanges = (Backend) json.instanceOf(Backend.class);
        backendCollection.change(backendWithChanges);
        farm.setVersion(backendWithChanges.getVersion());
        return this;
    }

    @Override
    public String get(String id) {
        if (id != null && !"".equals(id)) {
            return JsonObject.toJsonString(backendCollection.stream()
                    .filter(backend -> backend.getId().equals(id)));
        } else {
            return JsonObject.toJsonString(backendCollection);
        }
    }

}
