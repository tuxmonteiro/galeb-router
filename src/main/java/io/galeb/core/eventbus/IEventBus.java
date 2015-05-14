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
import io.galeb.core.model.Entity;
import io.galeb.core.model.Metrics;
import io.galeb.core.queue.QueueManager;

public interface IEventBus {

    public static final IEventBus NULL = new NullEventBus();

    public void publishEntity(Entity entity, String entityType, Action action);

    public void onRequestMetrics(Metrics metrics);

    public void onConnectionsMetrics(Metrics metrics);

    public IEventBus setEventBusListener(EventBusListener eventBusListener);

    public void start();

    public void stop();

    public MapReduce getMapReduce();

    public QueueManager getQueueManager();

    public String getClusterId();

}
