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

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class FarmTest {

    Farm farm;

    VirtualHost virtualhostNull = null;
    String virtualHostId = "test.localhost";
    String virtualHostId2 = "test2.localhost";
    JsonObject virtualHostIdJson;
    JsonObject virtualHostIdJson2;

    BackendPool backendPoolNull = null;
    String backendPoolId = "backendpool";
    String backendPoolId2 = "backendpool2";
    JsonObject backendPoolIdJson;
    JsonObject backendPoolIdJson2;

    Backend nullBackend = null;
    String backendId = "http://0.0.0.0:00";
    String backendId2 = "http://1.1.1.1:11";
    JsonObject backendIdJson;
    JsonObject backendIdJson2;

    String ruleId = "/";
    String ruleId2 = "/test";
    JsonObject ruleIdJson;
    JsonObject ruleIdJson2;

    @org.junit.Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        farm = new Farm();
        virtualHostIdJson = JsonObject.toJsonObject(new VirtualHost().setId(virtualHostId));
        virtualHostIdJson2 = JsonObject.toJsonObject(new VirtualHost().setId(virtualHostId2));

        backendPoolIdJson = JsonObject.toJsonObject(new BackendPool().setId(backendPoolId));
        backendPoolIdJson2 = JsonObject.toJsonObject(new BackendPool().setId(backendPoolId2));

        backendIdJson = JsonObject.toJsonObject(new Backend().setId(backendId).setParentId(backendPoolId));
        backendIdJson2 = JsonObject.toJsonObject(new Backend().setId(backendId2));

        ruleIdJson = JsonObject.toJsonObject(new Rule().setId(ruleId).setParentId(virtualHostId));
        ruleIdJson2 = JsonObject.toJsonObject(new Rule().setId(ruleId2));
    }

    @Test
    public void optionsDefaultIsEmptyAtFarm() {
        assertThat(farm.getOptions().isEmpty()).isTrue();
    }

    @Test
    public void setOptionsAtFarm() {
        final Map<String, String> newOptions = new HashMap<>();
        newOptions.put("key", "value");
        farm.setOptions(newOptions);
        assertThat(farm.getOptions().get("key")).isEqualTo("value");
    }

    @Test
    public void getEntityMapDefaultIsEmptyAtFarm() {
        assertThat(farm.getEntityMap()).isEmpty();
    }

    @Test
    public void getVirtualHostsAtFarm() {
        assertThat(farm.getCollection(VirtualHost.class)).isEmpty();
    }

    @Test
    public void getBackendPoolsAtFarm() {
        assertThat(farm.getCollection(BackendPool.class)).isEmpty();
    }

    @Test
    public void getBackendsAtFarm() {
        assertThat(farm.getCollection(Backend.class)).isEmpty();
    }

    @Test
    public void getRulesAtFarm() {
        assertThat(farm.getCollection(Rule.class)).isEmpty();
    }

    @Test
    public void getRootHandlerDefaultIsNullAtFarm() {
        assertThat(farm.getRootHandler()).isNull();
    }

}
