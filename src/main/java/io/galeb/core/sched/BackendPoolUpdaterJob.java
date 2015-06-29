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

import java.util.Comparator;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class BackendPoolUpdaterJob extends AbstractJob {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        setEnvironment(context.getJobDetail().getJobDataMap());

        try {
            farm.getCollection(BackendPool.class).stream()
                .filter(pool -> !((BackendPool) pool).getBackends().isEmpty())
                .forEach(pool -> {
                    Backend backendWithLeastConn = ((BackendPool) pool).getBackends().stream()
                                .min(Comparator.comparingInt(aBackend -> aBackend.getConnections())).get();
                    if (!backendWithLeastConn.equals(((BackendPool) pool).getBackendWithLeastConn())) {
                        ((BackendPool) pool).setBackendWithLeastConn(backendWithLeastConn);
                        farm.change(pool);
                    }
                });
        } catch (Exception e) {
            logger.error(e);
        }

        logger.trace(String.format("Job %s done.", this.getClass().getSimpleName()));
    }

}
