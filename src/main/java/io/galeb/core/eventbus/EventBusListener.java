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

import io.galeb.core.logging.Logger;
import io.galeb.core.logging.NullLogger;

public interface EventBusListener {

    public static final EventBusListener NULL = new EventBusListener() {

        private final Logger NULL_LOGGER = new NullLogger();

        @Override
        public void onEvent(Event event) {
            return;
        }

        @Override
        public IEventBus getEventBus() {
            return IEventBus.NULL;
        }

        @Override
        public Logger getLogger() {
            return NULL_LOGGER;
        };
    };

    public void onEvent(final Event event);

    public IEventBus getEventBus();

    public Logger getLogger();

}
