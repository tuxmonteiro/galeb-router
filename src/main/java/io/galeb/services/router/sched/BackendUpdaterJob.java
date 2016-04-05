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

import static io.galeb.core.util.Constants.SysProp.PROP_HOSTNAME;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import io.galeb.core.model.Backend;
import io.galeb.core.model.collections.BackendCollection;
import io.galeb.core.services.AbstractService;
import io.galeb.core.statsd.StatsdClient;
import io.galeb.core.util.map.ConnectionMapManager;

@DisallowConcurrentExecution
public class BackendUpdaterJob extends AbstractJob {

    private static final Logger LOGGER = LogManager.getLogger(BackendUpdaterJob.class);

    static {
        if (System.getProperty(PROP_HOSTNAME.toString())==null) {
            String hostname = PROP_HOSTNAME.def();
            if (hostname==null) {
                hostname="UNDEF";
            }
            System.setProperty(PROP_HOSTNAME.toString(), hostname);
        }
    }

    private static final long TTL = 10000L;
    private StatsdClient statsd;
    private final ConnectionMapManager connectionMapManager = ConnectionMapManager.INSTANCE;

    @Override
    protected void setEnvironment(JobDataMap jobDataMap) {
        super.setEnvironment(jobDataMap);
        if (statsd==null) {
            statsd = (StatsdClient) jobDataMap.get(AbstractService.STATSD);
        }
    }

    private void resetConnectionCounter(final BackendCollection backendCollection) {
        backendCollection.stream()
        .filter(backend -> ((Backend) backend).getConnections()>0 &&
                backend.getModifiedAt()<(System.currentTimeMillis()-TTL))
        .forEach(backend -> {
            backend.setVersion(farm.getVersion());
            ((Backend) backend).setConnections(0);
            backendCollection.change(backend);
        });
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        setEnvironment(context.getJobDetail().getJobDataMap());

        final BackendCollection backendCollection = (BackendCollection) farm.getCollection(Backend.class);

        resetConnectionCounter(backendCollection);

        connectionMapManager.reduce().forEach((backendID, numConnections) -> {
            backendCollection.stream()
                .filter(backend -> backend.getId().equals(backendID) &&
                        (backend.getModifiedAt() < (System.currentTimeMillis()-interval) ||
                            ((Backend)backend).getConnections() < 10) &&
                            ((Backend)backend).getConnections() != numConnections)
                .forEach(backend -> {
                    backend.setVersion(farm.getVersion());
                    ((Backend) backend).setConnections(numConnections);
                    backendCollection.change(backend);
                    farm.virtualhostsUsingBackend(backendID).stream()
                            .map(virtualhost -> virtualhost.getId())
                            .forEach(virtualhostId -> sendActiveConnections(virtualhostId, backendID, numConnections));
                    });
        });

        LOGGER.trace(String.format("Job %s done.", this.getClass().getSimpleName()));
    }

    private void sendActiveConnections(String virtualhostId, String backendId, int conn) {
        final String virtualhost = StatsdClient.cleanUpKey(virtualhostId);
        final String backend = StatsdClient.cleanUpKey(backendId);
        final String hostname = StatsdClient.cleanUpKey(System.getProperty(PROP_HOSTNAME.toString()));
        final String key = virtualhost + "." + backend + "." + hostname + "." + Backend.PROP_ACTIVECONN;
        statsd.gauge(key, conn);
    }
}
