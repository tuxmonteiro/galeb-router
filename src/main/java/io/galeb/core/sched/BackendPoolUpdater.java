package io.galeb.core.sched;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import io.galeb.core.eventbus.IEventBus;
import io.galeb.core.logging.Logger;
import io.galeb.core.model.Farm;

import java.util.UUID;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class BackendPoolUpdater implements JobListener {

    private Scheduler scheduler;

    private final Logger logger;
    private final Farm farm;
    private final IEventBus eventBus;

    private long schedulerInterval = 10000L;

    private boolean started = false;

    public BackendPoolUpdater(Farm farm, IEventBus eventBus, Logger logger) {
        this.farm = farm;
        this.eventBus = eventBus;
        this.logger = logger;
    }

    public boolean isStarted() {
        return started;
    }

    public void start() throws SchedulerException {
        setupScheduler();
        startJob();
        started = scheduler.isStarted();
    }

    private void setupScheduler() {
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.getListenerManager().addJobListener(this);
            scheduler.start();
        } catch (SchedulerException e) {
            logger.error(e);
        }
    }

    private void startJob() {
        try {
            if (scheduler.isStarted()) {

                Trigger trigger = newTrigger().withIdentity(UUID.randomUUID().toString())
                                              .startNow()
                                              .withSchedule(simpleSchedule().withIntervalInMilliseconds(schedulerInterval).repeatForever())
                                              .build();

                JobDataMap jobdataMap = new JobDataMap();
                jobdataMap.put("farm", farm);
                jobdataMap.put("logger", logger);
                jobdataMap.put("eventbus", eventBus);

                JobDetail job = newJob(BackendPoolUpdaterJob.class)
                                    .withIdentity(toString())
                                    .setJobData(jobdataMap)
                                    .build();


                scheduler.scheduleJob(job, trigger);

            }
        } catch (SchedulerException e) {
            logger.error(e);
        }
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
