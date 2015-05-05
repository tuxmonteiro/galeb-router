package io.galeb.core.sched;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import io.galeb.core.eventbus.IEventBus;
import io.galeb.core.logging.Logger;
import io.galeb.core.model.Farm;

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

public class QuartzScheduler implements JobListener {

    public static final String LOGGER   = "logger";
    public static final String FARM     = "farm";
    public static final String EVENTBUS = "eventbus";

    private final Farm farm;
    private final IEventBus eventBus;
    private final Logger logger;
    private final Scheduler scheduler;
    private boolean started = false;

    public QuartzScheduler(Farm farm, IEventBus eventBus, Logger logger) throws SchedulerException {
        this.farm = farm;
        this.eventBus = eventBus;
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
                jobdataMap.put(FARM, farm);
                jobdataMap.put(LOGGER, logger);
                jobdataMap.put(EVENTBUS, eventBus);

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
        logger.debug(context.getJobDetail().getKey().getName()+" to be executed");
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        logger.debug(context.getJobDetail().getKey().getName()+" vetoed");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context,
            JobExecutionException jobException) {
        logger.debug(context.getJobDetail().getKey().getName()+" was executed");
    }

}
