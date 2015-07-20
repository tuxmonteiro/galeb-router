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

package io.galeb.core.loadbalance;

import static io.galeb.core.loadbalance.LoadBalancePolicy.Algorithm.HASH;
import static io.galeb.core.loadbalance.LoadBalancePolicy.Algorithm.LEASTCONN;
import static io.galeb.core.loadbalance.LoadBalancePolicy.Algorithm.RANDOM;
import static io.galeb.core.loadbalance.LoadBalancePolicy.Algorithm.ROUNDROBIN;

import java.util.HashMap;
import java.util.Map;

import io.galeb.core.loadbalance.LoadBalancePolicy.Algorithm;
import io.galeb.core.loadbalance.impl.HashPolicy;
import io.galeb.core.loadbalance.impl.LeastConnPolicy;
import io.galeb.core.loadbalance.impl.RandomPolicy;
import io.galeb.core.loadbalance.impl.RoundRobinPolicy;
import io.galeb.core.model.BackendPool;

public class LoadBalancePolicyLocator {

    public static final Algorithm DEFAULT_ALGORITHM = ROUNDROBIN;

    private final Map<String, LoadBalancePolicy> loadbalanceCriterionMap = new HashMap<>();

    private volatile String loadBalanceAlgorithm = DEFAULT_ALGORITHM.toString();

    public LoadBalancePolicyLocator() {
        loadbalanceCriterionMap.put(ROUNDROBIN.toString(), new RoundRobinPolicy());
        loadbalanceCriterionMap.put(RANDOM.toString(), new RandomPolicy());
        loadbalanceCriterionMap.put(HASH.toString(), new HashPolicy());
        loadbalanceCriterionMap.put(LEASTCONN.toString(), new LeastConnPolicy());
    }

    public LoadBalancePolicy get() {
        LoadBalancePolicy loadBalancePolicy = loadbalanceCriterionMap.get(loadBalanceAlgorithm);
        if (loadBalancePolicy==null) {
            return LoadBalancePolicy.NULL;
        }
        return loadBalancePolicy;
    }

    public synchronized LoadBalancePolicyLocator setParams(final Map<String, Object> params) {
        if (params!=null) {
            String myLoadBalanceAlgorithm = (String) params.get(BackendPool.PROP_LOADBALANCE_POLICY);
            loadBalanceAlgorithm = myLoadBalanceAlgorithm==null ?
                    loadBalanceAlgorithm = DEFAULT_ALGORITHM.toString() : myLoadBalanceAlgorithm;
        }
        return this;
    }

}
