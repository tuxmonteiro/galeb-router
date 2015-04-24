package io.galeb.core.loadbalance.impl;

import static org.assertj.core.api.Assertions.assertThat;
import io.galeb.core.json.JsonObject;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;

import org.junit.Before;
import org.junit.Test;

public class RandomPolicyTest {

    int numBackends = 10;
    RandomPolicy randomPolicy;
    BackendPool backendPool;

    @Before
    public void setUp(){
        backendPool = new BackendPool();
        randomPolicy = new RandomPolicy();

        for (int x=0; x<numBackends; x++) {
            backendPool.addBackend(JsonObject.toJsonString(new Backend().setId(String.format("http://0.0.0.0:%s", x))));
        }
        randomPolicy.mapOfHosts(backendPool.getBackends().toArray());
    }

    @Test
    public void checkUniformDistribution() {
        long sum = 0;
        final double percentMarginOfError = 0.01;
        final long samples = 100000L;

        final long initialTime = System.currentTimeMillis();
        for (int x=0; x<samples; x++) {
            sum += randomPolicy.getChoice();
        }
        final long finishTime = System.currentTimeMillis();

        final double result = (numBackends*(numBackends-1)/2.0) * (1.0*samples/numBackends);

        System.out.println(String.format("RandomPolicy checkUniformDistribution: %d samples. Total time (ms): %d. NonUniformDistRatio%%: %.10f",
                    samples, finishTime-initialTime, Math.abs(100.0*(result-sum)/result)));

        final double topLimit = sum*(1.0+percentMarginOfError);
        final double bottomLimit = sum*(1.0-percentMarginOfError);

        assertThat(result).isGreaterThanOrEqualTo(bottomLimit)
                          .isLessThanOrEqualTo(topLimit);

    }

}
