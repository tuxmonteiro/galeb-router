package io.galeb.core.mapreduce;

import io.galeb.core.model.Metrics;

import java.util.Map;

public interface MapReduce {

    public MapReduce setTimeOut(Long timeOut);

    public Long getTimeOut();

    public void addMetrics(final Metrics metrics);

    public boolean contains(String backendId);

    public Map<String, Integer> reduce();

}
