package io.galeb.core.loadbalance.impl;

import io.galeb.core.loadbalance.LoadBalancePolicy;

public class RandomPolicy extends LoadBalancePolicy {

    @Override
    public int getChoice() {
        final int chosen = (int) (Math.random() * (uris.size() - Float.MIN_VALUE));
        last.lazySet(chosen);
        return chosen;
    }

}
