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

import javax.enterprise.inject.Alternative;

@Alternative
public class NullEventBus implements IEventBus {

    public static final MapReduce NULL_MAP_REDUCE = new NullMapReduce();

    @Override
    public void publishEntity(Entity entity, String entityType, Action action) {
        // NULL
    }

    @Override
    public void onRequestMetrics(Metrics metrics) {
        // NULL
    }

    @Override
    public void onConnectionsMetrics(Metrics metrics) {
        // NULL
    }

    @Override
    public IEventBus setEventBusListener(EventBusListener eventBusListener) {
        return this;
    }

    @Override
    public void start() {
        // NULL
    }

    @Override
    public void stop() {
        // NULL
    }

    @Override
    public MapReduce getMapReduce() {
        return NULL_MAP_REDUCE;
    }

    @Override
    public QueueManager getQueueManager() {
        return QueueManager.NULL;
    }

    @Override
    public String getClusterId() {
        return String.valueOf(null);
    }

}
