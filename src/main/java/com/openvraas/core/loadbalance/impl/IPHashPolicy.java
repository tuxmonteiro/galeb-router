package com.openvraas.core.loadbalance.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import com.openvraas.core.loadbalance.LoadBalancePolicy;
import com.openvraas.core.util.consistenthash.ConsistentHash;
import com.openvraas.core.util.consistenthash.HashAlgorithm;
import com.openvraas.core.util.consistenthash.HashAlgorithm.HashType;

public class IPHashPolicy extends LoadBalancePolicy {

    public static final String HASH_ALGORITHM = "HashAlgorithm";

    public static final String NUM_REPLICAS   = "NumReplicas";

    private HashAlgorithm hashAlgorithm = new HashAlgorithm(HashType.SIP24);

    private int numReplicas = 1;

    private final ConsistentHash<Integer> consistentHash = new ConsistentHash<Integer>(hashAlgorithm, numReplicas, new ArrayList<Integer>());

    private volatile String sourceIP = "127.0.0.1";

    @Override
    public int getChoice(final Object[] hosts) {
        if (needRebuild.get()) {
            Collection<Integer> hostsPos = new LinkedList<Integer>();
            for (int x=0;x<hosts.length;x++) {
                hostsPos.add(x);
            }
            consistentHash.rebuild(hashAlgorithm, numReplicas, hostsPos);
            needRebuild.compareAndSet(true, false);
        }
        int chosen = consistentHash.get(sourceIP);
        last.lazySet(chosen);
        return chosen;
    }

    @Override
    public synchronized LoadBalancePolicy setCriteria(final Map<String, Object> criteria) {
        String hashAlgorithmStr = (String) criteria.get(HASH_ALGORITHM);
        if (hashAlgorithmStr!=null) {
            if (HashAlgorithm.hashTypeFromString(hashAlgorithmStr)!=null) {
                hashAlgorithm = new HashAlgorithm(hashAlgorithmStr);
            }
        }
        String numReplicaStr = (String) criteria.get(NUM_REPLICAS);
        if (numReplicaStr!=null) {
            numReplicas = Integer.valueOf(numReplicaStr);
        }
        sourceIP = (String) criteria.get(SOURCE_IP_CRITERION);

        return this;
    }

    @Override
    public boolean needSourceIP() {
        return true;
    }

}
