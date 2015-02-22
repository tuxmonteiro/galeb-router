package com.openvraas.core.loadbalance;

import static com.openvraas.core.loadbalance.LoadBalancePolicy.Algorithm.*;

import java.util.HashMap;
import java.util.Map;

import com.openvraas.core.loadbalance.LoadBalancePolicy.Algorithm;
import com.openvraas.core.loadbalance.impl.IPHashPolicy;
import com.openvraas.core.loadbalance.impl.RandomPolicy;
import com.openvraas.core.loadbalance.impl.RoundRobinPolicy;

public class LoadBalancePolicyLocator {

    public static final Algorithm DEFAULT_ALGORITHM = ROUNDROBIN;

    private final Map<String, LoadBalancePolicy> loadbalanceCriterionMap = new HashMap<>();

    private volatile String loadBalanceAlgorithm = DEFAULT_ALGORITHM.toString();

    public LoadBalancePolicyLocator() {
        loadbalanceCriterionMap.put(ROUNDROBIN.toString(), new RoundRobinPolicy());
        loadbalanceCriterionMap.put(RANDOM.toString(), new RandomPolicy());
        loadbalanceCriterionMap.put(IPHASH.toString(), new IPHashPolicy());
    }

    public LoadBalancePolicy get() {
        LoadBalancePolicy loadBalancePolicy = loadbalanceCriterionMap.get(loadBalanceAlgorithm);
        if (loadBalancePolicy==null) {
            return LoadBalancePolicy.NULL;
        }
        return loadBalancePolicy;
    }

    public synchronized LoadBalancePolicyLocator setParams(final Map<String, Object> params) {
        if (params!=null) {
            String myLoadBalanceAlgorithm = (String) params.get(LoadBalancePolicy.LOADBALANCE_POLICY_FIELD);
            loadBalanceAlgorithm = myLoadBalanceAlgorithm==null ?
                    loadBalanceAlgorithm = DEFAULT_ALGORITHM.toString() : myLoadBalanceAlgorithm;
        }
        return this;
    }

}
