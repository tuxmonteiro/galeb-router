package io.galeb.core.statsd;

import javax.enterprise.inject.Alternative;

@Alternative
public class NullStatsdClient implements StatsdClient {
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
}