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

package io.galeb.core.util.consistenthash;

import static org.assertj.core.api.Assertions.assertThat;
import io.galeb.core.util.consistenthash.HashAlgorithm.HashType;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

public class ConsistentHashTest {

    ConsistentHash<Integer> consistentHash;
    HashAlgorithm hashAlgorithm;
    Map<Integer, Object> hosts = new TreeMap<Integer, Object>();
    int numReplicas = 1;
    int hashId = 1;
    int hashValue = 11;

    @Before
    public void setUp() {
        hashAlgorithm = new HashAlgorithm(HashType.SIP24);
        consistentHash = new ConsistentHash<Integer>(hashAlgorithm, numReplicas , hosts.keySet());
    }

    @Test
    public void addNodeAtConsistentehash() {
        consistentHash.add(hashId);
        assertThat(consistentHash.get(Integer.toString(hashId))).isNotNull();
    }

    @Test
    public void removeNodeAtConsistentehash() {
        consistentHash.add(hashId);
        assertThat(consistentHash.get(Integer.toString(hashId))).isNotNull();
        consistentHash.remove(hashId);
        assertThat(consistentHash.get(Integer.toString(hashId))).isNull();
    }

    @Test
    public void rebuildNodeAtConsistentehash() {
        assertThat(consistentHash.get(Integer.toString(hashId))).isNull();
        for (int x=0; x<10; x++) {
            hosts.put(x, x);
        }
        hosts.put(hashValue, hashId);
        consistentHash.rebuild(hashAlgorithm, numReplicas, hosts.keySet());
        assertThat(consistentHash.get(Integer.toString(hashId))).isEqualTo(hashValue);
    }

}
