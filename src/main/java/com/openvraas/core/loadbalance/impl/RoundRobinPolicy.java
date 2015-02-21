package com.openvraas.core.loadbalance.impl;

import com.openvraas.core.loadbalance.LoadBalancePolicy;

public class RoundRobinPolicy extends LoadBalancePolicy {

    @Override
    public int getChoice(final Object[] hosts) {
        return last.incrementAndGet() % hosts.length;
    }

    @Override
    public synchronized void reset() {
        last.set(0);
    }

}
