package io.galeb.core.sched;

import io.galeb.core.controller.EntityController.Action;
import io.galeb.core.eventbus.IEventBus;
import io.galeb.core.logging.Logger;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Farm;

import java.util.Collections;
import java.util.Comparator;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class BackendPoolUpdaterJob implements Job {

    private Logger logger;
    private Farm farm;
    private IEventBus eventBus;

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

        for (BackendPool backendPool: farm.getBackendPools()) {
            Backend backendWithLeastConn = Collections.min(backendPool.getBackends(),
                    new Comparator<Backend>() {
                        @Override
                        public int compare(Backend backend1, Backend backend2) {
                            return backend1.getConnections() - backend2.getConnections() ;
                        }
                    });

            BackendPool newBackendPool = new BackendPool(backendPool);
            newBackendPool.setBackendWithLeastConn(backendWithLeastConn);

            eventBus.publishEntity(newBackendPool, BackendPool.class.getSimpleName(), Action.CHANGE);
        }

        logger.debug(String.format("Job %s done.", this.getClass().getSimpleName()));
    }

}
