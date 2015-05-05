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
