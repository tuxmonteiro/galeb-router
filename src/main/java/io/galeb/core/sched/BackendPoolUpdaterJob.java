package io.galeb.core.sched;

import io.galeb.core.controller.EntityController.Action;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;

import java.util.Collections;
import java.util.Comparator;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class BackendPoolUpdaterJob extends AbstractJob {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        setEnvironment(context.getJobDetail().getJobDataMap());

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

            eventBus.publishEntity(newBackendPool,
                    BackendPool.class.getSimpleName().toLowerCase(), Action.CHANGE);
        }

        logger.debug(String.format("Job %s done.", this.getClass().getSimpleName()));
    }

}
