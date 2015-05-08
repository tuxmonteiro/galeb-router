package io.galeb.core.statsd;

public interface StatsdClient {

    public static final StatsdClient NULL = new StatsdClient() {
        @Override
        public void timing(String metricName, long value, double rate) {
            // NULL
        }

        @Override
        public void timing(String metricName, long value) {
            // NULL
        }

        @Override
        public void set(String metricName, String value, double rate) {
            // NULL
        }

        @Override
        public void set(String metricName, String value) {
            // NULL
        }

        @Override
        public StatsdClient server(String server) {
            return this;
        }

        @Override
        public StatsdClient port(int port) {
            return this;
        }

        @Override
        public void incr(String metricName, double rate) {
            // NULL
        }

        @Override
        public void incr(String metricName, int step, double rate) {
            // NULL
        }

        @Override
        public void incr(String metricName, int step) {
            // NULL
        }

        @Override
        public void incr(String metricName) {
            // NULL
        }

        @Override
        public void gauge(String metricName, double value, double rate) {
            // NULL
        }

        @Override
        public void gauge(String metricName, double value) {
            // NULL
        }

        @Override
        public void decr(String metricName, double rate) {
            // NULL
        }

        @Override
        public void decr(String metricName, int step, double rate) {
            // NULL
        }

        @Override
        public void decr(String metricName, int step) {
            // NULL
        }

        @Override
        public void decr(String metricName) {
            // NULL
        }

        @Override
        public void count(String metricName, int value, double rate) {
            // NULL
        }

        @Override
        public void count(String metricName, int value) {
            // NULL
        }

        @Override
        public StatsdClient prefix(String prefix) {
            return this;
        }
    };

    public StatsdClient server(String server);

    public StatsdClient port(int port);

    public StatsdClient prefix(String prefix);

    public void incr(String metricName);

    public void incr(String metricName, int step);

    public void incr(String metricName, int step, double rate);

    public void incr(String metricName, double rate);

    public void decr(String metricName);

    public void decr(String metricName, int step);

    public void decr(String metricName, int step, double rate);

    public void decr(String metricName, double rate);

    public void count(String metricName, int value);

    public void count(String metricName, int value, double rate);

    public void gauge(String metricName, double value);

    public void gauge(String metricName, double value, double rate);

    public void set(String metricName, String value);

    public void set(String metricName, String value, double rate);

    public void timing(String metricName, long value);

    public void timing(String metricName, long value, double rate);

}
