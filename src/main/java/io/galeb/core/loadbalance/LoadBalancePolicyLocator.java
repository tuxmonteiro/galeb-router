package io.galeb.core.loadbalance;

import static io.galeb.core.loadbalance.LoadBalancePolicy.Algorithm.IPHASH;
import static io.galeb.core.loadbalance.LoadBalancePolicy.Algorithm.LEASTCONN;
import static io.galeb.core.loadbalance.LoadBalancePolicy.Algorithm.RANDOM;
import static io.galeb.core.loadbalance.LoadBalancePolicy.Algorithm.ROUNDROBIN;
import io.galeb.core.loadbalance.LoadBalancePolicy.Algorithm;
import io.galeb.core.loadbalance.impl.IPHashPolicy;
import io.galeb.core.loadbalance.impl.LeastConnPolicy;
import io.galeb.core.loadbalance.impl.RandomPolicy;
import io.galeb.core.loadbalance.impl.RoundRobinPolicy;
import io.galeb.core.model.BackendPool;

import java.util.HashMap;
import java.util.Map;

public class LoadBalancePolicyLocator {

    public static final Algorithm DEFAULT_ALGORITHM = ROUNDROBIN;

    private final Map<String, LoadBalancePolicy> loadbalanceCriterionMap = new HashMap<>();

    private volatile String loadBalanceAlgorithm = DEFAULT_ALGORITHM.toString();

    public LoadBalancePolicyLocator() {
        loadbalanceCriterionMap.put(ROUNDROBIN.toString(), new RoundRobinPolicy());
        loadbalanceCriterionMap.put(RANDOM.toString(), new RandomPolicy());
        loadbalanceCriterionMap.put(IPHASH.toString(), new IPHashPolicy());
        loadbalanceCriterionMap.put(LEASTCONN.toString(), new LeastConnPolicy());
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
            String myLoadBalanceAlgorithm = (String) params.get(BackendPool.PROP_LOADBALANCE_POLICY);
            loadBalanceAlgorithm = myLoadBalanceAlgorithm==null ?
                    loadBalanceAlgorithm = DEFAULT_ALGORITHM.toString() : myLoadBalanceAlgorithm;
        }
        return this;
    }

}
