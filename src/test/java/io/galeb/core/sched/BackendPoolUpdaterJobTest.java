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
import io.galeb.core.controller.BackendPoolController;
import io.galeb.core.controller.EntityController.Action;
import io.galeb.core.eventbus.IEventBus;
import io.galeb.core.json.JsonObject;
import io.galeb.core.logging.Logger;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Farm;
import io.galeb.core.model.collections.BackendPoolCollection;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class BackendPoolUpdaterJobTest {

    private final JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);

    private final Farm farm = new Farm();

    private BackendPoolCollection backendPoolCollection;

    private class FakeEventBus implements IEventBus {
        @Override
        public void publishEntity(Entity entity, String entityType, Action action) {
            final BackendPoolController backendPoolController = new BackendPoolController(farm);
            final BackendPool backendPool = ((BackendPool)entity);
            try {
                backendPoolController.change(JsonObject.toJsonObject(backendPool));
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Before
    public void setUp() {
        final Logger logger = mock(Logger.class);
        final IEventBus eventBus = new FakeEventBus();
        backendPoolCollection = (BackendPoolCollection) farm.getCollection(BackendPool.class);
        doNothing().when(logger).error(any(Throwable.class));
        doNothing().when(logger).debug(any(Throwable.class));

        final JobDetail jobDetail = mock(JobDetail.class);
        when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);

        final JobDataMap jobdataMap = new JobDataMap();
        jobdataMap.put("farm", farm);
        jobdataMap.put("logger", logger);
        jobdataMap.put("eventbus", eventBus);
        when(jobDetail.getJobDataMap()).thenReturn(jobdataMap);
    }

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
        final Entity backendPool = farm.getCollection(BackendPool.class).getListByID(backendPoolId).get(0);
        final Backend backendWithLeastConn = ((BackendPool) backendPool).getBackendWithLeastConn();

        assertThat(backendWithLeastConn.getConnections()).isEqualTo(minConn);
    }

}
