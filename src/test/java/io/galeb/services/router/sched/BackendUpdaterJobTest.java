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

package io.galeb.services.router.sched;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import io.galeb.core.logging.Logger;
import io.galeb.core.model.Backend;
import io.galeb.core.model.Farm;
import io.galeb.core.services.AbstractService;
import io.galeb.core.statsd.StatsdClient;
import io.galeb.core.util.map.ConnectionMapManager;

public class BackendUpdaterJobTest {

    private final ConnectionMapManager connectionMapManager = ConnectionMapManager.INSTANCE;

    private static Farm farm = new Farm();

    private final JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);

    @Before
    public void setUp() {
        connectionMapManager.clear();

        final Logger logger = mock(Logger.class);
        final StatsdClient statsd = mock(StatsdClient.class);
        doNothing().when(logger).error(any(Throwable.class));
        doNothing().when(logger).debug(any(Throwable.class));

        final JobDetail jobDetail = mock(JobDetail.class);
        when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);

        final JobDataMap jobdataMap = new JobDataMap();
        jobdataMap.put(AbstractService.FARM, farm);
        jobdataMap.put(AbstractService.LOGGER, logger);
        jobdataMap.put(AbstractService.STATSD, statsd);
        jobdataMap.put(AbstractService.INTERVAL, 1000L);

        when(jobDetail.getJobDataMap()).thenReturn(jobdataMap);
    }

    @Test
    public void executeTest() throws JobExecutionException {
        final String backendPoolId = "pool1";
        final String backendTestedStr = "http://127.0.0.1:1";

        Backend backend = new Backend();
        backend.setConnections(0).setId(backendTestedStr).setParentId(backendPoolId);
        farm.add(backend);
        connectionMapManager.putOnCounterMap(backendTestedStr, backendTestedStr, 10);

        new BackendUpdaterJob().execute(jobExecutionContext);
        assertThat(backend.getConnections()).isEqualTo(10);
    }

}
