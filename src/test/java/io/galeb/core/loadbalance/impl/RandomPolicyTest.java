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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class RandomPolicyTest {

    int numBackends = 10;
    RandomPolicy randomPolicy;
    BackendPool backendPool;

    @Before
    public void setUp() throws URISyntaxException{
        backendPool = new BackendPool();
        randomPolicy = new RandomPolicy();
        final List<URI> uris = new LinkedList<>();

        for (int x=0; x<numBackends; x++) {
            final String backendId = String.format("http://0.0.0.0:%s", x);
            backendPool.addBackend(JsonObject.toJsonString(new Backend().setId(backendId)));
            uris.add(new URI(backendId));
        }
        randomPolicy.mapOfHosts(uris);
    }

    @Test
    public void checkUniformDistribution() {
        long sum = 0;
        final double percentMarginOfError = 0.01;
        final long samples = 100000L;

        final long initialTime = System.currentTimeMillis();
        for (int x=0; x<samples; x++) {
            sum += randomPolicy.getChoice();
        }
        final long finishTime = System.currentTimeMillis();

        final double result = (numBackends*(numBackends-1)/2.0) * (1.0*samples/numBackends);

        System.out.println(String.format("RandomPolicy checkUniformDistribution: %d samples. Total time (ms): %d. NonUniformDistRatio%%: %.10f",
                    samples, finishTime-initialTime, Math.abs(100.0*(result-sum)/result)));

        final double topLimit = sum*(1.0+percentMarginOfError);
        final double bottomLimit = sum*(1.0-percentMarginOfError);

        assertThat(result).isGreaterThanOrEqualTo(bottomLimit)
                          .isLessThanOrEqualTo(topLimit);

    }

}
