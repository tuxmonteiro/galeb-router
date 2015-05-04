package io.galeb.core.sched;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import io.galeb.core.controller.BackendController;
import io.galeb.core.controller.EntityController.Action;
import io.galeb.core.eventbus.EventBusListener;
import io.galeb.core.eventbus.IEventBus;
import io.galeb.core.json.JsonObject;
import io.galeb.core.logging.Logger;
import io.galeb.core.mapreduce.MapReduce;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Farm;
import io.galeb.core.model.Metrics;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class BackendUpdaterJobTest {

    private JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);

    private final Farm farm = new Farm();

    private int numBackends = 10;


    private class FakeEventBus implements IEventBus {
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
        public void onRequestMetrics(Metrics metrics) {
            // Fake
        }

        @Override
        public void onConnectionsMetrics(Metrics metrics) {
            // Fake
        }

        @Override
        public IEventBus setEventBusListener(EventBusListener eventBusListener) {
            // Fake
            return this;
        }

        @Override
        public void start() {
            // Fake
        }
    }

    private class FakeMapReduce implements MapReduce {
        @Override
        public MapReduce setTimeOut(Long timeOut) {
            // Fake
            return this;
        }

        @Override
        public Long getTimeOut() {
            // Fake
            return 0L;
        }

        @Override
        public void addMetrics(Metrics metrics) {
            // Fake
        }

        @Override
        public boolean contains(String backendId) {
            // Fake
            return true;
        }

        @Override
        public Map<String, Integer> reduce() {
            final Map<String, Integer> fakeMap = new HashMap<>();
            for (int count=1;count<=numBackends;count++) {
                fakeMap.put(String.format("http://127.0.0.1:%s", count), count);
            }
            return fakeMap;
        }
    }

    @Before
    public void setUp() {
        Logger logger = mock(Logger.class);
        IEventBus eventBus = new FakeEventBus();
        MapReduce mapReduce = new FakeMapReduce();
        doNothing().when(logger).error(any(Throwable.class));
        doNothing().when(logger).debug(any(Throwable.class));

        JobDetail jobDetail = mock(JobDetail.class);
        when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);

        JobDataMap jobdataMap = new JobDataMap();
        jobdataMap.put("farm", farm);
        jobdataMap.put("logger", logger);
        jobdataMap.put("eventbus", eventBus);
        jobdataMap.put("mapreduce", mapReduce);

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
