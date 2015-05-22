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
import io.galeb.core.model.collections.RuleCollection;

public class RuleController implements EntityController {

    private final Farm farm;

    private final RuleCollection ruleCollection;

    public RuleController(final Farm farm) {
        this.farm = farm;
        this.ruleCollection = (RuleCollection) farm.getRules();
    }

    @Override
    public EntityController add(JsonObject json) throws Exception {
        final Rule rule = (Rule) json.instanceOf(Rule.class);
        ruleCollection.add(rule);
        farm.setVersion(rule.getVersion());
        return this;
    }

    @Override
    public EntityController del(JsonObject json) throws Exception {
        final Rule rule = (Rule) json.instanceOf(Rule.class);
        ruleCollection.remove(rule);
        farm.setVersion(rule.getVersion());
        return this;
    }

    @Override
    public EntityController delAll() throws Exception {
        ruleCollection.clear();
        return this;
    }

    @Override
    public EntityController change(JsonObject json) throws Exception {
        final Rule rule = (Rule) json.instanceOf(Rule.class);
        ruleCollection.change(rule);
        return this;
    }

    @Override
    public String get(String id) {
        if (id != null && !"".equals(id)) {
            return JsonObject.toJsonString(ruleCollection.stream()
                        .filter(rule -> rule.getId().equals(id)));
        } else {
            return JsonObject.toJsonString(ruleCollection);
        }
    }

}
