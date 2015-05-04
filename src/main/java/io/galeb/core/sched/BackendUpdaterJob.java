package io.galeb.core.sched;

import io.galeb.core.controller.EntityController.Action;
import io.galeb.core.eventbus.IEventBus;
import io.galeb.core.logging.Logger;
import io.galeb.core.mapreduce.MapReduce;
import io.galeb.core.model.Backend;
import io.galeb.core.model.Farm;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class BackendUpdaterJob implements Job {

    private Logger logger;
    private Farm farm;
    private IEventBus eventBus;
    private MapReduce mapReduce;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        final JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        if (logger==null) {
            logger = (Logger) jobDataMap.get("logger");
        }
        if (farm==null) {
            farm = (Farm) jobDataMap.get("farm");
        }
        if (eventBus==null) {
            eventBus = (IEventBus) jobDataMap.get("eventbus");
        }
        if (mapReduce==null) {
            mapReduce = (MapReduce) jobDataMap.get("mapreduce");
        }

        Map<String, Integer> backEndMap = mapReduce.reduce();

        for (Entry<String, Integer> entry: backEndMap.entrySet()) {
            List<Backend> backends = farm.getBackend(entry.getKey());
            Backend backend = null;
            if (!backends.isEmpty() && backends.size()==1) {
                backend = backends.get(0);
            }
            backend.setConnections(entry.getValue());

            eventBus.publishEntity(backend, Backend.class.getSimpleName().toLowerCase(), Action.CHANGE);
        }

        logger.debug(String.format("Job %s done.", this.getClass().getSimpleName()));
    }

}
