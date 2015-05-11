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

package io.galeb.core.statsd;

public interface StatsdClient {

    public static final StatsdClient NULL = new StatsdClient() {
        @Override
        public void timing(String metricName, long value, double rate) {
            // NULL
        }

        @Override
        public void timing(String metricName, long value) {
            // NULL
        }

        @Override
        public void set(String metricName, String value, double rate) {
            // NULL
        }

        @Override
        public void set(String metricName, String value) {
            // NULL
        }

        @Override
        public StatsdClient server(String server) {
            return this;
        }

        @Override
        public StatsdClient port(int port) {
            return this;
        }

        @Override
        public void incr(String metricName, double rate) {
            // NULL
        }

        @Override
        public void incr(String metricName, int step, double rate) {
            // NULL
        }

        @Override
        public void incr(String metricName, int step) {
            // NULL
        }

        @Override
        public void incr(String metricName) {
            // NULL
        }

        @Override
        public void gauge(String metricName, double value, double rate) {
            // NULL
        }

        @Override
        public void gauge(String metricName, double value) {
            // NULL
        }

        @Override
        public void decr(String metricName, double rate) {
            // NULL
        }

        @Override
        public void decr(String metricName, int step, double rate) {
            // NULL
        }

        @Override
        public void decr(String metricName, int step) {
            // NULL
        }

        @Override
        public void decr(String metricName) {
            // NULL
        }

        @Override
        public void count(String metricName, int value, double rate) {
            // NULL
        }

        @Override
        public void count(String metricName, int value) {
            // NULL
        }

        @Override
        public StatsdClient prefix(String prefix) {
            return this;
        }
    };

    public StatsdClient server(String server);

    public StatsdClient port(int port);

    public StatsdClient prefix(String prefix);

    public void incr(String metricName);

    public void incr(String metricName, int step);

    public void incr(String metricName, int step, double rate);

    public void incr(String metricName, double rate);

    public void decr(String metricName);

    public void decr(String metricName, int step);

    public void decr(String metricName, int step, double rate);

    public void decr(String metricName, double rate);

    public void count(String metricName, int value);

    public void count(String metricName, int value, double rate);

    public void gauge(String metricName, double value);

    public void gauge(String metricName, double value, double rate);

    public void set(String metricName, String value);

    public void set(String metricName, String value, double rate);

    public void timing(String metricName, long value);

    public void timing(String metricName, long value, double rate);

}
