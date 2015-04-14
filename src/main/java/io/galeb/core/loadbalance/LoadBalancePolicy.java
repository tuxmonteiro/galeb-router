package io.galeb.core.loadbalance;

import io.galeb.core.util.SourceIP;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class LoadBalancePolicy {

    public enum Algorithm {
        ROUNDROBIN("RoundRobinPolicy"),
        RANDOM("RandomPolicy"),
        IPHASH("IPHashPolicy"),
        LEASTCONN("LeastConnPolicy");

        private String algoNameStr = "";

        Algorithm(String algoNameStr) {
            this.algoNameStr = algoNameStr;
        }

        @Override
        public String toString() {
            return algoNameStr;
        }
    }

    /** The Constant ALGORITHM_MAP. */
    private static final Map<String, Algorithm> ALGORITHM_MAP = new HashMap<>();
    static {
        for (final Algorithm algorithm : EnumSet.allOf(Algorithm.class)) {
            ALGORITHM_MAP.put(algorithm.toString(), algorithm);
        }
    }

    public static boolean hasLoadBalanceAlgorithm(String algorithmStr) {
        return ALGORITHM_MAP.containsKey(algorithmStr);
    }

    public static final String LOADBALANCE_POLICY_FIELD = "loadBalancePolicy";

    public static final String SOURCE_IP_CRITERION      = "SourceIP";

    private final Map<String, Object> loadBalancePolicyCriteria = new HashMap<>();

    protected final Map<Integer, Object> hosts = new TreeMap<Integer, Object>();

    protected AtomicInteger last = new AtomicInteger(0);

    private final AtomicBoolean needRebuild = new AtomicBoolean(true);

    public static LoadBalancePolicy NULL = new LoadBalancePolicy() {

        @Override
        public int getChoice() {
            return 0;
        }

        @Override
        public LoadBalancePolicy setCriteria(final Map<String, Object> criteria) {
            return this;
        }
    };

    public abstract int getChoice();

    public boolean needSourceIP() {
        return false;
    }

    public int getLastChoice() {
        return last.get();
    }

    public synchronized void reset() {
        last.lazySet(0);
        needRebuild.set(true);
    }

    public boolean isReseted() {
        return needRebuild.get();
    }

    public void rebuilt() {
        needRebuild.compareAndSet(true, false);
    }

    public LoadBalancePolicy setCriteria(final Map<String, Object> criteria) {
        if (criteria!=null && isReseted()) {
            loadBalancePolicyCriteria.putAll(criteria);
        }
        return this;
    }

    public LoadBalancePolicy setCriteria(final SourceIP sourceIP, Object extractable) {
        if (sourceIP!=null && extractable != null && needSourceIP()) {
            loadBalancePolicyCriteria.put(SOURCE_IP_CRITERION, sourceIP.pullFrom(extractable).get());
        }
        return this;
    }

    public LoadBalancePolicy mapOfHosts(final Object[] myHosts) {
        if (isReseted()) {
            hosts.clear();
            for (int x=0;x<myHosts.length;x++) {
                hosts.put(x, myHosts[x]);
            }
        }
        return this;
    }

}
