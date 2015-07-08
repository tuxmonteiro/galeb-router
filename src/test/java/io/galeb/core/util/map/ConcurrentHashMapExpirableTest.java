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

package io.galeb.core.util.map;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConcurrentHashMapExpirableTest {

    private static final long DEFAULT_TTL = 500L;

    private ConcurrentHashMapExpirable<String, Integer> concurrentHashMapExpirable;

    @Before
    public void setUp() {
        concurrentHashMapExpirable = new ConcurrentHashMapExpirable<>(DEFAULT_TTL, TimeUnit.MILLISECONDS);
    }

    @After
    public void cleanUp() {
        concurrentHashMapExpirable = null;
    }

    @Test
    public void putAndGetTest() {
        concurrentHashMapExpirable.put(toString(), Integer.MIN_VALUE);
        assertThat(concurrentHashMapExpirable.get(toString())).isEqualTo(Integer.MIN_VALUE);
    }

    @Test
    public void sizeTest() {
        concurrentHashMapExpirable.put(toString(), Integer.MIN_VALUE);
        assertThat(concurrentHashMapExpirable.size()).isEqualTo(1);
    }

    @Test
    public void isEmptyTest() {
        assertThat(concurrentHashMapExpirable).isEmpty();
    }

    @Test
    public void containsKeyTest() {
        concurrentHashMapExpirable.put(toString(), Integer.MIN_VALUE);
        assertThat(concurrentHashMapExpirable).containsKey(toString());
    }

    @Test
    public void containsValueTest() {
        concurrentHashMapExpirable.put(toString(), Integer.MIN_VALUE);
        assertThat(concurrentHashMapExpirable).containsValue(Integer.MIN_VALUE);
    }

    @Test
    public void removeTest() {
        concurrentHashMapExpirable.put(toString(), Integer.MIN_VALUE);
        assertThat(concurrentHashMapExpirable).containsValue(Integer.MIN_VALUE);
        concurrentHashMapExpirable.remove(toString());
        assertThat(concurrentHashMapExpirable).isEmpty();
    }

    @Test
    public void putAllTest() {
        Map<String, Integer> mapTemp = new HashMap<>();
        for (int x=0; x<10; x++) {
            mapTemp.put(Integer.toString(x), x);
        }
        concurrentHashMapExpirable.putAll(mapTemp);
        for (int x=0; x<10; x++) {
            assertThat(concurrentHashMapExpirable.get(Integer.toString(x))).isEqualTo(x);
        }
    }

    @Test
    public void clearTest() {
        for (int x=0; x<10; x++) {
            concurrentHashMapExpirable.put(Integer.toString(x), x);
        }
        concurrentHashMapExpirable.clear();
        assertThat(concurrentHashMapExpirable).isEmpty();
    }

    @Test
    public void keySetTest() {
        for (int x=0; x<10; x++) {
            concurrentHashMapExpirable.put(Integer.toString(x), x);
        }
        assertThat(concurrentHashMapExpirable.keySet()).hasSize(10);
    }

    @Test
    public void valuesTest() {
        for (int x=0; x<10; x++) {
            concurrentHashMapExpirable.put(Integer.toString(x), x);
        }
        assertThat(concurrentHashMapExpirable.values()).hasSize(10);
    }

    @Test
    public void entrySetTest() {
        for (int x=0; x<10; x++) {
            concurrentHashMapExpirable.put(Integer.toString(x), x);
        }
        assertThat(concurrentHashMapExpirable.entrySet()).hasSize(10);
    }

    @Test
    public void clearExpiredTest() throws InterruptedException {
        for (int x=0; x<10; x++) {
            concurrentHashMapExpirable.put(Integer.toString(x), x);
        }
        Thread.sleep(DEFAULT_TTL);
        concurrentHashMapExpirable.clearExpired();
        assertThat(concurrentHashMapExpirable).isEmpty();
    }

    @Test
    public void renewAllTest() throws InterruptedException {
        for (int x=0; x<10; x++) {
            concurrentHashMapExpirable.put(Integer.toString(x), x);
        }
        Thread.sleep(DEFAULT_TTL);
        concurrentHashMapExpirable.renewAll();
        concurrentHashMapExpirable.clearExpired();
        assertThat(concurrentHashMapExpirable.entrySet()).hasSize(10);
    }

    @Test
    public void reduceValueToInt() {
        int z = 0;
        for (int x=0; x<10; x++) {
            concurrentHashMapExpirable.put(Integer.toString(x), x);
            z = z + x;
        }
        assertThat(concurrentHashMapExpirable.reduceValuesToInt()).isEqualTo(z);
    }

}
