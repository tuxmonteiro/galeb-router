package io.galeb.core.loadbalance.impl;

import io.galeb.core.loadbalance.LoadBalancePolicy;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Farm;

import java.net.URI;
import java.util.Map;

public class LeastConnPolicy extends LoadBalancePolicy {

    private BackendPool backendPool = null;

    @Override
    public int getChoice() {
        if (backendPool!=null) {
            for (final URI uri: uris) {
                if (uri.toString().equals(backendPool.getBackendWithLeastConn().getId())) {
                    return uris.indexOf(uri);
                }
            }
        }
        return 0;
    }

    @Override
    public LoadBalancePolicy setCriteria(Map<String, Object> criteria) {
        super.setCriteria(criteria);
        final Farm farm = (Farm) loadBalancePolicyCriteria.get(Farm.class.getSimpleName());
        final String backendPoolId = (String) loadBalancePolicyCriteria.get(BackendPool.class.getSimpleName());
        backendPool = farm.getBackendPool(backendPoolId);
        return this;
    }

}
