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

public interface EntityController {

    public enum Action {
        ADD,
        DEL,
        DEL_ALL,
        CHANGE,
        UNKNOWN
    }

    public static final EntityController NULL = new EntityController() {

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

    @Deprecated
    public EntityController add(JsonObject json) throws Exception;

    @Deprecated
    public EntityController del(JsonObject json) throws Exception;

    public EntityController delAll() throws Exception;

    @Deprecated
    public EntityController change(JsonObject json) throws Exception;

    public String get(String id);

    public default EntityController add(Entity entity) throws Exception {
        return this;
    }

    public default EntityController del(Entity entity) throws Exception {
        return this;
    }

    public default EntityController change(Entity entity) throws Exception {
        return this;
    }

}
