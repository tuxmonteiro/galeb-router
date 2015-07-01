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

import static io.galeb.core.loadbalance.LoadBalancePolicy.Algorithm.LEASTCONN;
import static org.assertj.core.api.Assertions.assertThat;
import io.galeb.core.loadbalance.LoadBalancePolicy;
import io.galeb.core.loadbalance.LoadBalancePolicyLocator;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Farm;
import io.galeb.core.model.collections.BackendPoolCollection;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class LeastConnPolicyTest {

    private LoadBalancePolicy leastConnPolicy;
    private final String backendPoolId = "pool1";
    private Farm farm;

    private BackendPoolCollection backendPoolCollection;


    @Before
    public void setUp() {
        final BackendPool backendPool = (BackendPool) new BackendPool().setId(backendPoolId);
        farm = new Farm();
        backendPoolCollection = (BackendPoolCollection) farm.getCollection(BackendPool.class);
        backendPoolCollection.add(backendPool);

        final Map<String, Object> criteria = new HashMap<>();
        criteria.put(BackendPool.class.getSimpleName(), backendPool.getId());
        criteria.put(Farm.class.getSimpleName(), farm);
        criteria.put(BackendPool.PROP_LOADBALANCE_POLICY, LEASTCONN.toString());

        leastConnPolicy  = new LoadBalancePolicyLocator().setParams(criteria).get();
        leastConnPolicy.setCriteria(criteria);
    }

    @Test
    public void getChoiceTest() {
        final int numBackends = 10;
        final int maxConn = 1000;
        int minConn = maxConn;

        final Entity backendPool = backendPoolCollection.getListByID(backendPoolId).get(0);
        final LinkedList<String> uris = new LinkedList<>();

        for (int pos=0; pos<=numBackends;pos++) {
            final int numConn = (int) (Math.random() * (maxConn - Float.MIN_VALUE));

            final String backendId = "http://"+UUID.randomUUID().toString();
            final Backend backend = (Backend)new Backend().setConnections(numConn)
                                                    .setParentId(backendPoolId)
                                                    .setId(backendId);
            ((BackendPool) backendPool).addBackend(backend);
            if (numConn < minConn) {
                minConn = numConn;
                ((BackendPool) backendPool).setBackendWithLeastConn(backend);
            }
            uris.add(backendId);
        }
        leastConnPolicy.mapOfHosts(uris);

        final Backend chosen = (Backend) ((BackendPool) backendPool)
                                    .getBackends().toArray()[leastConnPolicy.getChoice()];

        assertThat(minConn).isEqualTo(chosen.getConnections());
    }

}
