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

package io.galeb.core.loadbalance.impl;

import static org.assertj.core.api.Assertions.assertThat;
import io.galeb.core.json.JsonObject;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

public class RoundRobinPolicyTest {

    int numBackends = 10;
    RoundRobinPolicy roundRobinPolicy;
    BackendPool backendPool;

    @Before
    public void setUp() {
        backendPool = new BackendPool();
        roundRobinPolicy = new RoundRobinPolicy();
        final LinkedList<String> uris = new LinkedList<>();

        for (int x=0; x<numBackends; x++) {
            final String backendId = String.format("http://0.0.0.0:%s", x);
            backendPool.addBackend(JsonObject.toJsonString(new Backend().setId(backendId)));
            uris.add(backendId);
        }
        roundRobinPolicy.mapOfHosts(uris);
    }


    @Test
    public void backendsChosenInSequence() {
        final LinkedList<Object> controlList = new LinkedList<>();
        for (int counter=0; counter<numBackends*99; counter++) {
            controlList.add(roundRobinPolicy.getChoice());
        }

        roundRobinPolicy.reset();
        int lastChoice = roundRobinPolicy.getLastChoice();
        int currentChoice;

        for (int counter=0; counter<numBackends*99; counter++) {
            currentChoice = (int) controlList.poll();
            assertThat(currentChoice).isNotEqualTo(lastChoice);
            assertThat(roundRobinPolicy.getChoice()).isEqualTo(currentChoice);
            lastChoice = currentChoice;
        }
    }

}
