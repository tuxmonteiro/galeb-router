package com.openvraas.core.loadbalance;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
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
        for (Algorithm algorithm : EnumSet.allOf(Algorithm.class)) {
            ALGORITHM_MAP.put(algorithm.toString(), algorithm);
        }
    }

    public static boolean hasLoadBalanceAlgorithm(String algorithmStr) {
        return ALGORITHM_MAP.containsKey(algorithmStr);
    }

    public static final String    LOADBALANCE_POLICY_FIELD = "loadBalancePolicy";

    public static final Algorithm DEFAULT_ALGORITHM        = Algorithm.ROUNDROBIN;

    public static final String    SOURCE_IP_CRITERION      = "SourceIP";


    protected AtomicInteger last = new AtomicInteger(0);
    protected AtomicBoolean needRebuild = new AtomicBoolean(true);

    public static LoadBalancePolicy NULL = new LoadBalancePolicy() {

        @Override
        public int getChoice(Object[] hosts) {
            return 0;
        }

        @Override
        public LoadBalancePolicy setCriteria(final Map<String, Object> criteria) {
            return this;
        }
    };

    public abstract int getChoice(final Object[] hosts);

    public boolean needSourceIP() {
        return false;
    }

    public int getLastChoice() {
        return last.get();
    };

    public synchronized void reset() {
        last.lazySet(0);
        needRebuild.set(true);
    }

    public LoadBalancePolicy setCriteria(final Map<String, Object> criteria) {
        return this;
    };

}
