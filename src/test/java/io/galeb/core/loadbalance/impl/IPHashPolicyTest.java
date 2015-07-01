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
import io.galeb.core.loadbalance.LoadBalancePolicy;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.util.consistenthash.HashAlgorithm;
import io.galeb.core.util.consistenthash.HashAlgorithm.HashType;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IPHashPolicyTest {

    int numBackends = 10;
    IPHashPolicy ipHashPolicy;
    BackendPool backendPool;
    Map<String, Object> criteria;

    @Before
    public void setUp() {
        backendPool = new BackendPool();
        ipHashPolicy = new IPHashPolicy();
        final LinkedList<String> uris = new LinkedList<>();

        for (int x=0; x<numBackends; x++) {
            uris.add(String.format("http://0.0.0.0:%s", x));
            backendPool.addBackend(JsonObject.toJsonString(new Backend().setId(String.format("http://0.0.0.0:%s", x))));
        }
        ipHashPolicy.mapOfHosts(uris);
        criteria = new HashMap<String, Object>();
    }

    @After
    public void tearDown() {
        criteria.clear();
    }


    @Test
    public void checkUniformDistribution() {
        final long samples = 10000L;
        final int rounds = 5;
        final double percentMarginOfError = 0.5;
        final int numClients = 100;
        final Set<HashType> hashs = EnumSet.allOf(HashAlgorithm.HashType.class);

        for (int round=0; round < rounds; round++) {
            System.out.println(String.format("IPHashPolicyTest.checkUniformDistribution - round %s: %d samples", round+1, samples));

            for (final HashType hash: hashs) {

                criteria.put(IPHashPolicy.HASH_ALGORITHM, hash.toString());

                long sum = 0L;
                final long initialTime = System.currentTimeMillis();
                for (Integer counter=0; counter<samples; counter++) {
                    final int chosen = (int) (Math.random() * (numClients - Float.MIN_VALUE));
                    criteria.put(LoadBalancePolicy.SOURCE_IP_CRITERION, Integer.toString(chosen));
                    ipHashPolicy.setCriteria(criteria);
                    sum += ipHashPolicy.getChoice();
                }

                final long finishTime = System.currentTimeMillis();

                final double result = (numBackends*(numBackends-1)/2.0) * (samples/numBackends);

                System.out.println(String.format("-> TestHashPolicy.checkUniformDistribution (%s): Time spent (ms): %d. NonUniformDistRatio (smaller is better): %.4f%%",
                        hash, finishTime-initialTime, Math.abs(100.0*(result-sum)/result)));

                final double topLimit = sum*(1.0+percentMarginOfError);
                final double bottomLimit = sum*(1.0-percentMarginOfError);

                assertThat(result).isGreaterThanOrEqualTo(bottomLimit)
                                  .isLessThanOrEqualTo(topLimit);
            }
        }
    }

}

