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

package io.galeb.core.mapreduce;

import java.util.Collections;
import java.util.Map;

public interface MapReduce {

    public default MapReduce setTimeOut(Long timeOut) {
        return this;
    }

    public default Long getTimeOut() {
        return -1L;
    }

    public default void addMetrics(String key, int value) {
        // NULL
    }

    public default boolean contains(String backendId) {
        return false;
    }

    public default Map<String, Integer> reduce() {
        return Collections.emptyMap();
    }

}
