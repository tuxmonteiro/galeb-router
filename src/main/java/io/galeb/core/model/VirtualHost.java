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

public class VirtualHost extends Entity {

    private static final long serialVersionUID = 1L;

    @Expose private Set<Rule> rules = new CopyOnWriteArraySet<>();

    public VirtualHost() {
        super();
    }

    public VirtualHost(VirtualHost virtualhost) {
        super(virtualhost);
        setRules(virtualhost.getRules());
        updateHash();
    }

    public void setRules(Set<Rule> arules) {
        final Set<Rule> copyRules = new HashSet<>(arules);
        rules.clear();
        rules.addAll(copyRules);
    }

    public Rule getRule(String ruleId) {
        Rule rule = null;
        for (Rule ruleTemp: rules) {
            if (ruleId.equals(ruleTemp.getId())) {
                rule = ruleTemp;
                break;
            }
        }
        return rule;
    }

    public Rule newRule(String json) {
        Rule rule = (Rule) JsonObject.fromJson(json, Rule.class);
        addRule(rule);
        return rule;
    }

    public VirtualHost addRule(Rule rule) {
        rules.add(rule);
        return this;
    }

    public VirtualHost delRule(String ruleId) {
        Rule rule = getRule(ruleId);
        if (rule!=null) {
            rules.remove(rule);
        }
        return this;
    }

    public boolean containRule(String ruleId) {
        return getRule(ruleId) != null;
    }

    public void clearRules() {
        rules.clear();
    }

    public Set<Rule> getRules() {
        return rules;
    }

    @Override
    public Entity copy() {
        return new VirtualHost(this);
    }

}
