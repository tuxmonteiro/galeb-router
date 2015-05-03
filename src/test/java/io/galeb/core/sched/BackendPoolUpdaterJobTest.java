package io.galeb.core.sched;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import io.galeb.core.eventbus.IEventBus;
import io.galeb.core.logging.Logger;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Farm;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class BackendPoolUpdaterJobTest {

    private JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);

    private Farm farm = new Farm();

    @Before
    public void setUp() {
        Logger logger = mock(Logger.class);
        doNothing().when(logger).error(any(Throwable.class));
        doNothing().when(logger).debug(any(Throwable.class));

        JobDetail jobDetail = mock(JobDetail.class);
        when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);

        JobDataMap jobdataMap = new JobDataMap();
        jobdataMap.put("farm", farm);
        jobdataMap.put("logger", logger);
        jobdataMap.put("eventbus", IEventBus.NULL);
        when(jobDetail.getJobDataMap()).thenReturn(jobdataMap);
    }

    @Test
    public void executeTest() throws JobExecutionException {
        final String backendPoolId = "pool1";
        int numBackends = 10;
        int maxConn = 1000;
        int minConn = maxConn;

        farm.addBackendPool(((BackendPool)new BackendPool().setId(backendPoolId)));

        for (int x=0; x<=numBackends;x++) {
            int numConn = (int) (Math.random() * (maxConn - Float.MIN_VALUE));
            minConn = numConn < minConn ? numConn : minConn;

            Backend backend = (Backend)new Backend().setConnections(numConn)
                                                    .setParentId(backendPoolId)
                                                    .setId(UUID.randomUUID().toString());
            farm.addBackend(backend);
        }

        new BackendPoolUpdaterJob().execute(jobExecutionContext);
        Backend backendWithLeastConn = farm.getBackendPool(backendPoolId).getBackendWithLeastConn();

        assertThat(backendWithLeastConn.getConnections()).isEqualTo(minConn);
    }

}
