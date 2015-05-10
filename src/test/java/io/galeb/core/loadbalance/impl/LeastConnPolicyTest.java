package io.galeb.core.loadbalance.impl;

import static io.galeb.core.loadbalance.LoadBalancePolicy.Algorithm.LEASTCONN;
import static org.assertj.core.api.Assertions.assertThat;
import io.galeb.core.loadbalance.LoadBalancePolicy;
import io.galeb.core.loadbalance.LoadBalancePolicyLocator;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Farm;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class LeastConnPolicyTest {

    private LoadBalancePolicy leastConnPolicy;
    private final String backendPoolId = "pool1";
    private Farm farm;

    @Before
    public void setUp() {
        final BackendPool backendPool = (BackendPool) new BackendPool().setId(backendPoolId);
        farm = new Farm().addBackendPool(backendPool);

        final Map<String, Object> criteria = new HashMap<>();
        criteria.put(BackendPool.class.getSimpleName(), backendPool.getId());
        criteria.put(Farm.class.getSimpleName(), farm);
        criteria.put(BackendPool.PROP_LOADBALANCE_POLICY, LEASTCONN.toString());

        leastConnPolicy  = new LoadBalancePolicyLocator().setParams(criteria).get();
        leastConnPolicy.setCriteria(criteria);
    }

    @Test
    public void getChoiceTest() throws URISyntaxException {
        final int numBackends = 10;
        final int maxConn = 1000;
        int minConn = maxConn;
        final BackendPool backendPool = farm.getBackendPool(backendPoolId);
        final List<URI> uris = new LinkedList<>();

        for (int pos=0; pos<=numBackends;pos++) {
            final int numConn = (int) (Math.random() * (maxConn - Float.MIN_VALUE));

            final String backendId = "http://"+UUID.randomUUID().toString();
            final Backend backend = (Backend)new Backend().setConnections(numConn)
                                                    .setParentId(backendPoolId)
                                                    .setId(backendId);
            backendPool.addBackend(backend);
            if (numConn < minConn) {
                minConn = numConn;
                backendPool.setBackendWithLeastConn(backend);
            }
            uris.add(new URI(backendId));
        }
        leastConnPolicy.mapOfHosts(uris);

        final Backend chosen = (Backend) backendPool.getBackends().toArray()[leastConnPolicy.getChoice()];

        assertThat(minConn).isEqualTo(chosen.getConnections());
    }

}
