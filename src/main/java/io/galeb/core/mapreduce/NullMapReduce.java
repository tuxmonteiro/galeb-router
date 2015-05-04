package io.galeb.core.mapreduce;

import io.galeb.core.model.Metrics;

import java.util.Collections;
import java.util.Map;

import javax.enterprise.inject.Alternative;

@Alternative
public class NullMapReduce implements MapReduce {

    @Override
    public MapReduce setTimeOut(Long timeOut) {
        return this;
    }

    @Override
    public Long getTimeOut() {
        return -1L;
    }

    @Override
    public void addMetrics(Metrics metrics) {
        // NULL
    }

    @Override
    public boolean contains(String backendId) {
        return false;
    }

    @Override
    public Map<String, Integer> reduce() {
        return Collections.emptyMap();
    }

}
