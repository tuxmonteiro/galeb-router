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

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.UUID;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import io.galeb.core.logging.Logger;
import io.galeb.core.model.Farm;
import io.galeb.core.services.AbstractService;
import io.galeb.core.statsd.StatsdClient;

public class QuartzScheduler implements JobListener {

    private final Farm farm;
    private final StatsdClient statsd;
    private final Logger logger;
    private final Scheduler scheduler;
    private boolean started = false;

    public QuartzScheduler(Farm farm,
                           StatsdClient statsd,
                           Logger logger) throws SchedulerException {
        this.farm = farm;
        this.statsd = statsd;
        this.logger = logger;
        scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.getListenerManager().addJobListener(this);
        scheduler.start();
        started = scheduler.isStarted();
    }

    public boolean isStarted() {
        return started;
    }

    public QuartzScheduler startPeriodicJob(Class<? extends Job> jobClass, long interval) {
        try {
            if (scheduler.isStarted()) {

                Trigger trigger = newTrigger().withIdentity(UUID.randomUUID().toString())
                                              .startNow()
                                              .withSchedule(simpleSchedule().withIntervalInMilliseconds(interval).repeatForever())
                                              .build();

                JobDataMap jobdataMap = new JobDataMap();
                jobdataMap.put(AbstractService.FARM, farm);
                jobdataMap.put(AbstractService.LOGGER, logger);
                jobdataMap.put(AbstractService.STATSD, statsd);
                jobdataMap.put(AbstractService.INTERVAL, interval);

                JobDetail job = newJob(jobClass).withIdentity(jobClass.getSimpleName()+this)
                                                .setJobData(jobdataMap)
                                                .build();

                scheduler.scheduleJob(job, trigger);
            }
        } catch (SchedulerException e) {
            logger.error(e);
        }

        return this;
    }

    @Override
    public String getName() {
        return toString();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        logger.trace(context.getJobDetail().getKey().getName()+" to be executed");
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        logger.trace(context.getJobDetail().getKey().getName()+" vetoed");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context,
            JobExecutionException jobException) {
        logger.trace(context.getJobDetail().getKey().getName()+" was executed");
    }

}
