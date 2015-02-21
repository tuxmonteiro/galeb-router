package com.openvraas.core.loadbalance.impl;

import com.openvraas.core.loadbalance.LoadBalancePolicy;

public class RandomPolicy extends LoadBalancePolicy {

    @Override
    public int getChoice(Object[] hosts) {
        int chosen = (int) (Math.random() * (hosts.length - Float.MIN_VALUE));
        last.lazySet(chosen);
        return chosen;
    }

}
