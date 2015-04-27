package io.galeb.core.metrics;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class CounterConnections {

    public static class Data {
        private final String key;
        private final Integer total;

        public Data(String key, Integer total) {
            this.key = key;
            this.total = total;
        }

        public String getKey() {
            return key;
        }

        public Integer getTotal() {
            return total;
        }

        @Override
        public String toString() {
            return String.format("%s:%d", key, total);
        }
    }

    private static final Queue<Data> METRICS_QUEUE = new ConcurrentLinkedQueue<>();

    private static final List<CounterConnectionsListener> LISTENERS = new CopyOnWriteArrayList<>();

    private CounterConnections() {
        // static only
    }

    public static void registerListener(CounterConnectionsListener listener) {
        LISTENERS.add(listener);
    }

    public static void updateMap(String key, Integer total) {
        METRICS_QUEUE.add(new Data(key, total));
        notifyListeners();
    }

    private static void notifyListeners() {
        for (final CounterConnectionsListener counterConnectionsListener: LISTENERS) {
            counterConnectionsListener.hasNewData();
        }
    }

    public static Data poolData() {
        return METRICS_QUEUE.poll();
    }

    public static boolean hasData() {
        return !METRICS_QUEUE.isEmpty();
    }

}
