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

package io.galeb.core.eventbus;

import io.galeb.core.controller.EntityController.Action;
import io.galeb.core.mapreduce.MapReduce;
import io.galeb.core.mapreduce.NullMapReduce;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Metrics;
import io.galeb.core.queue.QueueManager;

public interface IEventBus {

    public static final MapReduce NULL_MAP_REDUCE = new NullMapReduce();

    public static final IEventBus NULL = new NullEventBus();

    public default void publishEntity(Entity entity, String entityType, Action action) {
        // NULL
    }

    public default void onRequestMetrics(Metrics metrics) {
        // NULL
    }

    public default IEventBus setEventBusListener(EventBusListener eventBusListener) {
        return this;
    }

    public default void start() {
        // NULL
    }

    public default void stop() {
        // NULL
    }

    public default MapReduce getMapReduce() {
        return NULL_MAP_REDUCE;
    }

    public default QueueManager getQueueManager() {
        return QueueManager.NULL;
    }

    public default String getClusterId() {
        return String.valueOf(null);
    }

}
