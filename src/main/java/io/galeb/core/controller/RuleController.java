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
import io.galeb.core.model.Farm;
import io.galeb.core.model.Rule;

public class RuleController implements EntityController {

    private final Farm farm;

    public RuleController(final Farm farm) {
        this.farm = farm;
    }

    @Override
    public EntityController add(JsonObject json) throws Exception {
        farm.addRule(json);
        farm.setVersion(((Rule) json.instanceOf(Rule.class)).getVersion());
        return this;
    }

    @Override
    public EntityController del(JsonObject json) throws Exception {
        farm.delRule(json);
        farm.setVersion(((Rule) json.instanceOf(Rule.class)).getVersion());
        return this;
    }

    @Override
    public EntityController delAll() throws Exception {
        for (final Rule rule: farm.getRules()) {
            del(JsonObject.toJsonObject(rule));
        };
        return this;
    }

    @Override
    public EntityController change(JsonObject json) throws Exception {
        farm.delRule(json);
        farm.addRule(json);
        return this;
    }

    @Override
    public String get(String id) {
        if (id != null && !"".equals(id)) {
            return JsonObject.toJsonString(farm.getRules(id));
        } else {
            return JsonObject.toJsonString(farm.getRules());
        }
    }

}
