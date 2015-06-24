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

package io.galeb.core.sched;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import io.galeb.core.cluster.DistributedMap;
import io.galeb.core.logging.Logger;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Farm;
import io.galeb.core.model.collections.BackendPoolCollection;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class BackendPoolUpdaterJobTest {

    private final JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);

    private final Farm farm = new Farm();

    private BackendPoolCollection backendPoolCollection;
    private DistributedMap<String, Entity> distributedMap;

    @Before
    public void setUp() {
        final Logger logger = mock(Logger.class);
        distributedMap = new DistributedMap<String, Entity>() {
            @Override
            public ConcurrentMap<String, Entity> getMap(String key) {
                return new ConcurrentHashMap<String, Entity>();
            }
        };
        backendPoolCollection = (BackendPoolCollection) farm.getCollection(BackendPool.class);
        doNothing().when(logger).error(any(Throwable.class));
        doNothing().when(logger).debug(any(Throwable.class));

        final JobDetail jobDetail = mock(JobDetail.class);
        when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);

        final JobDataMap jobdataMap = new JobDataMap();
        jobdataMap.put(QuartzScheduler.FARM, farm);
        jobdataMap.put(QuartzScheduler.LOGGER, logger);
        jobdataMap.put(QuartzScheduler.DISTRIBUTEDMAP, distributedMap);
        when(jobDetail.getJobDataMap()).thenReturn(jobdataMap);
    }

    @Ignore
    @Test
    public void executeTest() throws JobExecutionException {
        final String backendPoolId = "pool1";
        final int numBackends = 10;
        final int maxConn = 1000;
        int minConn = maxConn;

        backendPoolCollection.add(new BackendPool().setId(backendPoolId));

        for (int x=0; x<=numBackends;x++) {
            final int numConn = (int) (Math.random() * (maxConn - Float.MIN_VALUE));
            minConn = numConn < minConn ? numConn : minConn;

            final Backend backend = (Backend)new Backend().setConnections(numConn)
                                                    .setParentId(backendPoolId)
                                                    .setId(UUID.randomUUID().toString());
            farm.add(backend);
        }

        new BackendPoolUpdaterJob().execute(jobExecutionContext);
        final Entity backendPool =  farm.getCollection(BackendPool.class).getListByID(backendPoolId).get(0);
        final Backend backendWithLeastConn = ((BackendPool) backendPool).getBackendWithLeastConn();

        assertThat(backendWithLeastConn.getConnections()).isEqualTo(minConn);
    }

}
