package io.galeb.core.sched;

import io.galeb.core.controller.EntityController.Action;
import io.galeb.core.mapreduce.MapReduce;
import io.galeb.core.model.Backend;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class BackendUpdaterJob extends AbstractJob {

    private MapReduce mapReduce;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        setEnvironment(context.getJobDetail().getJobDataMap());
        mapReduce = eventBus.getMapReduce();

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
