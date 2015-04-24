package io.galeb.core.loadbalance.impl;

import static org.assertj.core.api.Assertions.assertThat;
import io.galeb.core.json.JsonObject;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

public class RoundRobinPolicyTest {

    int numBackends = 10;
    RoundRobinPolicy roundRobinPolicy;
    BackendPool backendPool;

    @Before
    public void setUp(){
        backendPool = new BackendPool();
        roundRobinPolicy = new RoundRobinPolicy();

        for (int x=0; x<numBackends; x++) {
            backendPool.addBackend(JsonObject.toJsonString(new Backend().setId(String.format("http://0.0.0.0:%s", x))));
        }
        roundRobinPolicy.mapOfHosts(backendPool.getBackends().toArray());
    }


    @Test
    public void backendsChosenInSequence() {
        final LinkedList<Object> controlList = new LinkedList<>();
        for (int counter=0; counter<numBackends*99; counter++) {
            controlList.add(roundRobinPolicy.getChoice());
        }

        roundRobinPolicy.reset();
        int lastChoice = roundRobinPolicy.getLastChoice();
        int currentChoice;

        for (int counter=0; counter<numBackends*99; counter++) {
            currentChoice = (int) controlList.poll();
            assertThat(currentChoice).isNotEqualTo(lastChoice);
            assertThat(roundRobinPolicy.getChoice()).isEqualTo(currentChoice);
            lastChoice = currentChoice;
        }
    }

}
