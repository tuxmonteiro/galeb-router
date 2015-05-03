package io.galeb.core.loadbalance.impl;

import io.galeb.core.loadbalance.LoadBalancePolicy;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;

import java.util.Map;

public class LeastConnPolicy extends LoadBalancePolicy {

    private BackendPool backendPool = null;

    @Override
    public int getChoice() {
        int chosen = 0;
        if (backendPool!=null) {
            for (Object backendObj: hosts.values()) {
                if (backendObj instanceof Backend &&
                        ((Backend)backendObj).equals(backendPool.getBackendWithLeastConn())) {
                    return chosen;
                }
                chosen++;
            }
        }
        return 0;
    }

    @Override
    public LoadBalancePolicy setCriteria(Map<String, Object> criteria) {
        super.setCriteria(criteria);
        final Object backendPoolObj = loadBalancePolicyCriteria.get(BackendPool.class.getSimpleName());
        if (backendPoolObj instanceof BackendPool) {
            backendPool = (BackendPool) backendPoolObj;
        }
        return this;
    }

}
