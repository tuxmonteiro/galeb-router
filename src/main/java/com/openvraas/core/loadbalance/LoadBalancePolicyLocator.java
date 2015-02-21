package com.openvraas.core.loadbalance;

import static com.openvraas.core.loadbalance.LoadBalancePolicy.Algorithm.*;

import java.util.HashMap;
import java.util.Map;

import com.openvraas.core.loadbalance.impl.IPHashPolicy;
import com.openvraas.core.loadbalance.impl.RandomPolicy;
import com.openvraas.core.loadbalance.impl.RoundRobinPolicy;

public class LoadBalancePolicyLocator {

    Map<String, LoadBalancePolicy> loadbalanceCriterionMap = new HashMap<>();

    public LoadBalancePolicyLocator() {
        loadbalanceCriterionMap.put(ROUNDROBIN.toString(), new RoundRobinPolicy());
        loadbalanceCriterionMap.put(RANDOM.toString(), new RandomPolicy());
        loadbalanceCriterionMap.put(IPHASH.toString(), new IPHashPolicy());
    }

    public LoadBalancePolicy get(String loadBalanceAlgorithm) {
        LoadBalancePolicy loadBalancePolicy = loadbalanceCriterionMap.get(loadBalanceAlgorithm);
        if (loadBalancePolicy==null) {
            loadBalancePolicy = LoadBalancePolicy.NULL;
        }
        return loadBalancePolicy;
    }

}
