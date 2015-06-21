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
import io.galeb.core.model.Entity;
import io.galeb.core.model.Farm;

public abstract class EntityController {

    private final Farm farm;

    public enum Action {
        ADD,
        DEL,
        DEL_ALL,
        CHANGE,
        UNKNOWN
    }

    private static final String CONTROLLER_NAME_SUFFIX = "controller";

    public static final EntityController NULL = new EntityController(null) {

        @Override
        public String get(String id) {
            return "NULL";
        }

        @Override
        public EntityController del(JsonObject json) {
            return this;
        }

        @Override
        public EntityController delAll() {
            return this;
        }

        @Override
        public EntityController change(JsonObject json) {
            return this;
        }

        @Override
        public EntityController add(JsonObject json) {
            return this;
        }
    };

    public EntityController(Farm farm) {
        this.farm = farm;
    }

    public static String getControllerName(Class<?> clazz) {
        return clazz.getSimpleName().toLowerCase().replace(CONTROLLER_NAME_SUFFIX, "");
    }

    @Deprecated
    public abstract EntityController add(JsonObject json) throws Exception;

    @Deprecated
    public abstract EntityController del(JsonObject json) throws Exception;

    public abstract EntityController delAll() throws Exception;

    @Deprecated
    public abstract EntityController change(JsonObject json) throws Exception;

    public EntityController add(Entity entity) throws Exception {
        farm.add(entity);
        farm.setVersion(entity.getVersion());
        return this;    }

    public EntityController del(Entity entity) throws Exception {
        farm.del(entity);
        farm.setVersion(entity.getVersion());
        return this;
    }

    public EntityController delAll(Class<? extends Entity> clazz) {
        farm.clear(clazz);
        return this;
    }

    public EntityController change(Entity entity) throws Exception {
        farm.change(entity);
        farm.setVersion(entity.getVersion());
        return this;
    }

    public String get(String id) {
        return JsonObject.NULL.toString();
    }

    public String get(Class<? extends Entity> clazz, String id) {
        if (clazz.equals(Farm.class)) {
            return JsonObject.toJsonString(farm);
        }
        if (id != null && !"".equals(id)) {
            return JsonObject.toJsonString(farm.getCollection(clazz).stream()
                        .filter(entity -> entity.getId().equals(id)));
        } else {
            return JsonObject.toJsonString(farm.getCollection(clazz));
        }
    }

    void setVersion(int version) {
        farm.setVersion(version);
    }

}
