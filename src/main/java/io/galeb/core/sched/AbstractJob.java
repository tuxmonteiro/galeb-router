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

import io.galeb.core.eventbus.IEventBus;
import io.galeb.core.logging.Logger;
import io.galeb.core.model.Farm;

import org.quartz.Job;
import org.quartz.JobDataMap;

public abstract class AbstractJob implements Job {

    protected Logger logger;
    protected Farm farm;
    protected IEventBus eventBus;

    protected void setEnvironment(final JobDataMap jobDataMap) {
        if (logger==null) {
            logger = (Logger) jobDataMap.get(QuartzScheduler.LOGGER);
        }
        if (farm==null) {
            farm = (Farm) jobDataMap.get(QuartzScheduler.FARM);
        }
        if (eventBus==null) {
            eventBus = (IEventBus) jobDataMap.get(QuartzScheduler.EVENTBUS);
        }
    }
}
