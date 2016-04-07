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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;

import io.galeb.core.model.Farm;
import io.galeb.core.services.AbstractService;

public abstract class AbstractJob implements Job {

    private static final Logger LOGGER = LogManager.getLogger();

    protected Farm farm;
    protected long interval = 0L;

    @SuppressWarnings("unchecked")
    protected void setEnvironment(final JobDataMap jobDataMap) {
        if (farm==null) {
            farm = (Farm) jobDataMap.get(AbstractService.FARM_KEY);
        }
        if (interval<=0L) {
            interval = jobDataMap.getLongValue(AbstractService.INTERVAL);
        }
    }
}
