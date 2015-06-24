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

import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Entity;

import java.util.Collections;
import java.util.Comparator;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class BackendPoolUpdaterJob extends AbstractJob {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        setEnvironment(context.getJobDetail().getJobDataMap());

        if (!clusterEvents.isReady()) {
            return;
        }

        for (final Entity backendPool: farm.getCollection(BackendPool.class)) {
            if (((BackendPool) backendPool).getBackends().isEmpty()) {
                continue;
            }
            final Backend backendWithLeastConn = Collections.min(((BackendPool) backendPool).getBackends(),
                    new Comparator<Backend>() {
                        @Override
                        public int compare(Backend backend1, Backend backend2) {
                            return backend1.getConnections() - backend2.getConnections() ;
                        }
                    });

            if (backendWithLeastConn !=null) {

                final Backend backendWithLeastConnOrig = ((BackendPool) backendPool).getBackendWithLeastConn();
                boolean hasChange = false;
                if (backendWithLeastConnOrig==null) {
                    hasChange = true;
                } else {
                    if (!backendWithLeastConnOrig.equals(backendWithLeastConn)) {
                        hasChange = true;
                    }
                }
                if (hasChange) {
                    final BackendPool newBackendPool = new BackendPool((BackendPool) backendPool);
                    newBackendPool.setBackendWithLeastConn(backendWithLeastConn);
                    newBackendPool.setVersion(farm.getVersion());
                    distributedMap.getMap(BackendPool.class.getName()).put(newBackendPool.getId(), newBackendPool);
                }
            }
        }

        logger.trace(String.format("Job %s done.", this.getClass().getSimpleName()));
    }

}
