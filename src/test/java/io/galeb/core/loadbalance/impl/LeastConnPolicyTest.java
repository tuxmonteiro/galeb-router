package io.galeb.core.loadbalance.impl;

import static io.galeb.core.loadbalance.LoadBalancePolicy.Algorithm.LEASTCONN;
import static org.assertj.core.api.Assertions.assertThat;
import io.galeb.core.loadbalance.LoadBalancePolicy;
import io.galeb.core.loadbalance.LoadBalancePolicyLocator;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class LeastConnPolicyTest {

    private LoadBalancePolicy leastConnPolicy;
    private BackendPool backendPool;
    private final String backendPoolId = "pool1";

    @Before
    public void setUp() {
        backendPool = (BackendPool) new BackendPool().setId(backendPoolId);

        Map<String, Object> criteria = new HashMap<>();
        criteria.put(BackendPool.class.getSimpleName(), backendPool);
        criteria.put(LoadBalancePolicy.LOADBALANCE_POLICY_FIELD, LEASTCONN.toString());

        leastConnPolicy  = new LoadBalancePolicyLocator().setParams(criteria).get();
        leastConnPolicy.setCriteria(criteria);
    }

    @Test
    public void getChoiceTest() {
        int numBackends = 10;
        int maxConn = 1000;
        int minConn = maxConn;

        for (int pos=0; pos<=numBackends;pos++) {
            int numConn = (int) (Math.random() * (maxConn - Float.MIN_VALUE));

            Backend backend = (Backend)new Backend().setConnections(numConn)
                                                    .setParentId(backendPoolId)
                                                    .setId(UUID.randomUUID().toString());
            backendPool.addBackend(backend);
            if (numConn < minConn) {
                minConn = numConn;
                backendPool.setBackendWithLeastConn(backend);
            }
        }
        Object[] backends = backendPool.getBackends().toArray();
        leastConnPolicy.mapOfHosts(backends);

        Backend chosen = (Backend) backends[leastConnPolicy.getChoice()];

        assertThat(minConn).isEqualTo(chosen.getConnections());
    }

}
