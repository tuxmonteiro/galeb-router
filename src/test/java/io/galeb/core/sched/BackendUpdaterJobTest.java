package io.galeb.core.sched;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import io.galeb.core.controller.BackendController;
import io.galeb.core.controller.EntityController.Action;
import io.galeb.core.eventbus.IEventBus;
import io.galeb.core.eventbus.NullEventBus;
import io.galeb.core.json.JsonObject;
import io.galeb.core.logging.Logger;
import io.galeb.core.mapreduce.MapReduce;
import io.galeb.core.mapreduce.NullMapReduce;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Farm;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class BackendUpdaterJobTest {

    private static Farm farm = new Farm();

    private static int numBackends = 10;

    private JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);

    private static class FakeMapReduce extends NullMapReduce {
        @Override
        public Map<String, Integer> reduce() {
            final Map<String, Integer> fakeMap = new HashMap<>();
            for (int count=1;count<=numBackends;count++) {
                fakeMap.put(String.format("http://127.0.0.1:%s", count), count);
            }
            return fakeMap;
        }
    }

    private static class FakeEventBus extends NullEventBus {
        @Override
        public void publishEntity(Entity entity, String entityType, Action action) {
            BackendController backendController = new BackendController(farm);
            Backend backend = ((Backend)entity);
            try {
                backendController.change(JsonObject.toJsonObject(backend));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        public MapReduce getMapReduce() {
            return new FakeMapReduce();
        }
    }

    @Before
    public void setUp() {
        farm.clearBackendPool();
        farm.clearVirtualHosts();

        Logger logger = mock(Logger.class);
        IEventBus eventBus = new FakeEventBus();
        doNothing().when(logger).error(any(Throwable.class));
        doNothing().when(logger).debug(any(Throwable.class));

        JobDetail jobDetail = mock(JobDetail.class);
        when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);

        JobDataMap jobdataMap = new JobDataMap();
        jobdataMap.put(QuartzScheduler.FARM, farm);
        jobdataMap.put(QuartzScheduler.LOGGER, logger);
        jobdataMap.put(QuartzScheduler.EVENTBUS, eventBus);

        when(jobDetail.getJobDataMap()).thenReturn(jobdataMap);
    }

    @Test
    public void executeTest() throws JobExecutionException {
        final String backendPoolId = "pool1";
        String backendTestedStr = "http://127.0.0.1:1";

        farm.addBackendPool(((BackendPool)new BackendPool().setId(backendPoolId)));

        for (int count=1;count<=numBackends;count++) {
            Backend backend = (Backend)new Backend().setConnections(0)
                                                    .setParentId(backendPoolId)
                                                    .setId(String.format("http://127.0.0.1:%s", count));
            farm.addBackend(backend);
        }

        new BackendUpdaterJob().execute(jobExecutionContext);
        BackendPool backendPool = farm.getBackendPool(backendPoolId);
        Backend backendTested = backendPool.getBackend(backendTestedStr);

        assertThat(backendTested.getConnections()).isGreaterThan(0);
    }

}
