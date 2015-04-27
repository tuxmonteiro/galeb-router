package io.galeb.core.metrics;

import static org.junit.Assert.*;
import io.galeb.core.metrics.CounterConnections.Data;

import org.junit.Test;

public class CounterConnectionsTest {

    class FakeCounterConnectionsListener implements CounterConnectionsListener {

        private Data lastData;

        public Data getLastData() {
            return lastData;
        }

        @Override
        public void hasNewData() {
            lastData = CounterConnections.poolData();
        }

    }

    @Test
    public void counterConnectionsTest() {
        final String key = "TEST";
        final int total = 1974;
        final FakeCounterConnectionsListener counterConnectionsListener = new FakeCounterConnectionsListener();
        CounterConnections.registerListener(counterConnectionsListener);
        CounterConnections.updateMap(key, total);

        final Data data = counterConnectionsListener.getLastData();
        assertEquals(data.toString(), String.format("%s:%d", key, total));
    }

}
