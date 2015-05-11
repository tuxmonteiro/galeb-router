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

import static org.assertj.core.api.Assertions.assertThat;
import io.galeb.core.json.JsonObject;

import org.junit.Before;
import org.junit.Test;

public class VirtualHostTest {

    VirtualHost virtualHost;
    Rule nullRule = null;
    String ruleId = "/";
    String ruleId2 = "/test";
    String ruleIdJson;
    String ruleIdJson2;

    @Before
    public void setUp() {
        virtualHost = new VirtualHost();
        ruleIdJson = JsonObject.toJsonString(new Rule().setId(ruleId));
        ruleIdJson2 = JsonObject.toJsonString(new Rule().setId(ruleId2));
    }

    @Test
    public void getRulesAtVirtualHost() {
        assertThat(virtualHost.getRules()).isEmpty();
    }

    @Test
    public void clearRulesAtVirtualHost() {
        virtualHost.newRule(ruleIdJson);
        assertThat(virtualHost.getRules()).hasSize(1);
        virtualHost.clearRules();
        assertThat(virtualHost.getRules()).isEmpty();
    }

    @Test
    public void containRuleIsFalseInDefaultRulesAtVirtualHost() {
        assertThat(virtualHost.containRule(ruleId)).isFalse();
    }

    @Test
    public void containRuleIsTrueAfterAddRuleAtVirtualHost() {
        virtualHost.newRule(ruleIdJson);
        assertThat(virtualHost.containRule(ruleId)).isTrue();
    }

    @Test
    public void delRuleAtVirtualHost() {
        virtualHost.newRule(ruleIdJson);
        assertThat(virtualHost.getRules()).hasSize(1);
        virtualHost.delRule(ruleId);
        assertThat(virtualHost.getRules()).isEmpty();
    }

    @Test
    public void delNullRuleAtVirtualHost() {
        virtualHost.newRule(ruleIdJson);
        assertThat(virtualHost.getRules()).hasSize(1);
        virtualHost.delRule(ruleIdJson2);
        assertThat(virtualHost.getRules()).hasSize(1);
    }

    @Test
    public void getSingleRuleAtVirtualHost() {
        virtualHost.newRule(ruleIdJson);
        virtualHost.newRule(ruleIdJson2);
        assertThat(virtualHost.getRule(ruleId)).isInstanceOf(Rule.class);
    }

}
