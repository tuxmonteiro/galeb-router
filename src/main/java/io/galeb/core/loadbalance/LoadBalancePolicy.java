/*
 * Copyright (c) 2014-2015 Globo.com - ATeam
 * All rights reserved.
 *
 * This source is subject to the Apache License, Version 2.0.
 * Please see the LICENSE file for more information.
 *
 * Authors: See AUTHORS file
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.galeb.core.loadbalance;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import io.galeb.core.loadbalance.hash.ExtractableKey;
import io.galeb.core.loadbalance.hash.KeyTypeLocator;

public abstract class LoadBalancePolicy {

    public enum Algorithm {
        ROUNDROBIN("RoundRobin"),
        RANDOM("Random"),
        HASH("Hash"),
        STRICT_LEASTCONN("StrictLeastConn"),
        LEASTCONN("LeastConn");

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

    public static final String  PROP_KEY = "keyType";

    protected final Map<String, Object> loadBalancePolicyCriteria = new HashMap<>();

    protected AtomicInteger last = new AtomicInteger(0);

    private final AtomicBoolean needRebuild = new AtomicBoolean(true);

    protected LinkedList<String> uris = new LinkedList<>();

    protected volatile Optional<String> aKey = Optional.empty();

    protected KeyTypeLocator keyTypeLocator = KeyTypeLocator.NULL;

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

    public LoadBalancePolicy mapOfHosts(final LinkedList<String> uris) {
        if (isReseted()) {
            this.uris = uris;
        }
        return this;
    }

    public LoadBalancePolicy extractKeyFrom(final Object extractable) {
        final Optional<String> aKeyType = Optional.ofNullable((String) loadBalancePolicyCriteria.get(PROP_KEY));
        final ExtractableKey extractableKey = keyTypeLocator.getKey(aKeyType.orElse(KeyTypeLocator.DEFAULT_KEY_TYPE));
        aKey = Optional.ofNullable(extractableKey.get(extractable));
        return this;
    }

    public LoadBalancePolicy setKeyTypeLocator(final KeyTypeLocator keyTypeLocator) {
        this.keyTypeLocator = keyTypeLocator;
        return this;
    }

}
