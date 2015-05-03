package io.galeb.core.sched;

import io.galeb.core.logging.Logger;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Farm;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class BackendPoolUpdaterJob implements Job {

    private Logger logger;
    private Farm farm;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        final JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        if (logger==null) {
            logger = (Logger) jobDataMap.get("logger");
        }
        if (farm==null) {
            farm = (Farm) jobDataMap.get("farm");
        }

        Set<BackendPool> backendPools = farm.getBackendPools();
        for (BackendPool backendPool: backendPools) {
            Set<Backend> backends = backendPool.getBackends();
            Backend backendWithLeastConn = Collections.min(backends, new Comparator<Backend>() {
                @Override
                public int compare(Backend backend1, Backend backend2) {
                    return backend1.getConnections() - backend2.getConnections() ;
                }
            });
            backendPool.setBackendWithLeastConn(backendWithLeastConn);
        }

        logger.debug(String.format("Job %s done.", this.getClass().getSimpleName()));
    }

}
