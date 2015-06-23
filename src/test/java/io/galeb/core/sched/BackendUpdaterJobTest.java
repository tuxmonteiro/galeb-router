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
import io.galeb.core.controller.BackendController;
import io.galeb.core.controller.EntityController.Action;
import io.galeb.core.eventbus.IEventBus;
import io.galeb.core.eventbus.NullEventBus;
import io.galeb.core.json.JsonObject;
import io.galeb.core.logging.Logger;
import io.galeb.core.mapreduce.MapReduce;
import io.galeb.core.mapreduce.NullMapReduce;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Farm;
import io.galeb.core.model.VirtualHost;
import io.galeb.core.model.collections.BackendPoolCollection;
import io.galeb.core.model.collections.VirtualHostCollection;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class BackendUpdaterJobTest {

    private static Farm farm = new Farm();

    private BackendPoolCollection backendPoolCollection;
    private VirtualHostCollection virtualHostCollection;

    private static int numBackends = 10;

    private final JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);

    private static class FakeMapReduce extends NullMapReduce {
        @Override
        public Map<String, Integer> reduce() {
            final Map<String, Integer> fakeMap = new HashMap<>();
            for (int count=1;count<=numBackends;count++) {
                fakeMap.put(String.format("http://127.0.0.1:%s", count), count);
            }
            return fakeMap;
        }
    }

    private static class FakeEventBus extends NullEventBus {
        @Override
        public void publishEntity(Entity entity, String entityType, Action action) {
            final BackendController backendController = new BackendController(farm);
            final Backend backend = ((Backend)entity);
            try {
                backendController.change(JsonObject.toJsonObject(backend));
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        public MapReduce getMapReduce() {
            return new FakeMapReduce();
        }
    }

    @Before
    public void setUp() {
        backendPoolCollection = (BackendPoolCollection) farm.getCollection(BackendPool.class);
        backendPoolCollection.clear();

        virtualHostCollection = (VirtualHostCollection) farm.getCollection(VirtualHost.class);
        virtualHostCollection.clear();

        final Logger logger = mock(Logger.class);
        final IEventBus eventBus = new FakeEventBus();
        final DistributedMap<String, Entity> distributedMap = new DistributedMap<String, Entity>() {
            @Override
            public ConcurrentMap<String, Entity> getMap(String key) {
                return new ConcurrentHashMap<String, Entity>();
            }
        };
        doNothing().when(logger).error(any(Throwable.class));
        doNothing().when(logger).debug(any(Throwable.class));

        final JobDetail jobDetail = mock(JobDetail.class);
        when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);

        final JobDataMap jobdataMap = new JobDataMap();
        jobdataMap.put(QuartzScheduler.FARM, farm);
        jobdataMap.put(QuartzScheduler.LOGGER, logger);
        jobdataMap.put(QuartzScheduler.EVENTBUS, eventBus);
        jobdataMap.put(QuartzScheduler.DISTRIBUTEDMAP, distributedMap);

        when(jobDetail.getJobDataMap()).thenReturn(jobdataMap);
    }

    @Test
    public void executeTest() throws JobExecutionException {
        final String backendPoolId = "pool1";
        final String backendTestedStr = "http://127.0.0.1:1";

        backendPoolCollection.add(new BackendPool().setId(backendPoolId));

        for (int count=1;count<=numBackends;count++) {
            final Backend backend = (Backend)new Backend().setConnections(0)
                                                    .setParentId(backendPoolId)
                                                    .setId(String.format("http://127.0.0.1:%s", count));
            farm.add(backend);
        }

        new BackendUpdaterJob().execute(jobExecutionContext);
        final Entity backendPool = farm.getCollection(BackendPool.class).getListByID(backendPoolId).get(0);
        final Backend backendTested = ((BackendPool) backendPool).getBackend(backendTestedStr);

        assertThat(backendTested.getConnections()).isGreaterThan(0);
    }

}
