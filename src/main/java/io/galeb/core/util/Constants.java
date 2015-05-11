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

package io.galeb.core.util;

public final class Constants {

    public static final String TRUE  = Boolean.toString(true);

    public static final String FALSE = Boolean.toString(false);

    public enum SysProp {
        PROP_ENABLE_ACCESSLOG  ("io.galeb.accesslog"        , FALSE),
        PROP_MAXCONN           ("io.galeb.maxConn"          , String.valueOf(1000)),
        PROP_SCHEDULER_INTERVAL("io.galeb.schedulerInterval", String.valueOf(1000));

        private final String name;
        private final String defaultStr;

        SysProp(String name, String defaultStr) {
            this.name = name;
            this.defaultStr = defaultStr;
        }

        @Override
        public String toString() {
            return name;
        }

        public String def() {
            return defaultStr;
        }
    }

    private Constants() {
        // static dictionary only
    }

}
