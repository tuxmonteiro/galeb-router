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
import io.galeb.core.model.collections.BackendPoolCollection;

public class BackendPoolController implements EntityController {

    private final Farm farm;

    private final BackendPoolCollection backendPoolCollection;

    public BackendPoolController(final Farm farm) {
        this.farm = farm;
        this.backendPoolCollection = (BackendPoolCollection) farm.getBackendPools();
    }

    @Override
    public EntityController add(JsonObject json) throws Exception {
        final BackendPool backendPool = (BackendPool) json.instanceOf(BackendPool.class);
        backendPoolCollection.add(backendPool);
        farm.setVersion(backendPool.getVersion());
        return this;
    }

    @Override
    public EntityController del(JsonObject json) throws Exception {
        final BackendPool backendPool = (BackendPool) json.instanceOf(BackendPool.class);
        backendPoolCollection.remove(backendPool);
        farm.setVersion(backendPool.getVersion());
        return this;
    }

    @Override
    public EntityController delAll() throws Exception {
        backendPoolCollection.clear();
        return null;
    }

    @Override
    public EntityController change(JsonObject json) throws Exception {
        final BackendPool backendPool = (BackendPool) json.instanceOf(BackendPool.class);
        backendPoolCollection.change(backendPool);
        farm.setVersion(backendPool.getVersion());
        return this;
    }

    @Override
    public String get(String id) {
        if (id != null && !"".equals(id)) {
            return JsonObject.toJsonString(backendPoolCollection.stream()
                    .filter(backendPool -> backendPool.getId().equals(id)));
        } else {
            return JsonObject.toJsonString(backendPoolCollection);
        }
    }

}
