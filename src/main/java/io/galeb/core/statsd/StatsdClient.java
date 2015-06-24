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

    static final String STATSD_HOST   = "host";
    static final String STATSD_PORT   = "port";
    static final String STATSD_PREFIX = "prefix";

    static final String PREFIX = StatsdClient.class.getPackage().getName();

    static void setup() {
        if (System.getProperty(PREFIX + "." + STATSD_HOST)==null) {
            System.setProperty(PREFIX + "." + STATSD_HOST, "127.0.0.1");
        }
        if (System.getProperty(PREFIX + "." + STATSD_PORT)==null) {
            System.setProperty(PREFIX + "." + STATSD_PORT, "8125");
        }
        if (System.getProperty(PREFIX + "." + STATSD_PREFIX)==null) {
            System.setProperty(PREFIX + "." + STATSD_PREFIX, "galeb");
        }
    }

    static String getHost() {
        setup();
        return System.getProperty(PREFIX + "." + STATSD_HOST);
    }

    static String getPort() {
        setup();
        return System.getProperty(PREFIX + "." + STATSD_PORT);
    }

    static String getPrefix() {
        setup();
        return System.getProperty(PREFIX + "." + STATSD_PREFIX);
    }

    static String cleanUpKey(String key) {
        return key.replaceAll("http://", "").replaceAll("[.:]", "_");
    }

    public static final String PROP_STATUSCODE = "status";
    public static final String PROP_HTTPCODE_PREFIX = "httpCode";
    public static final String PROP_REQUESTTIME = "requestTime";
    public static final String PROP_REQUESTTIME_AVG = "requestTimeAvg";

    public default StatsdClient host(String server) {
        return this;
    }

    public default StatsdClient port(int port) {
        return this;
    }

    public default StatsdClient prefix(String prefix) {
        return this;
    }

    public default void incr(String metricName) {
        // default
    }

    public default void incr(String metricName, int step) {
        // default
    }

    public default void incr(String metricName, int step, double rate) {
        // default
    }

    public default void incr(String metricName, double rate) {
        // default
    }

    public default void decr(String metricName) {
        // default
    }

    public default void decr(String metricName, int step) {
        // default
    }

    public default void decr(String metricName, int step, double rate) {
        // default
    }

    public default void decr(String metricName, double rate) {
        // default
    }

    public default void count(String metricName, int value) {
        // default
    }

    public default void count(String metricName, int value, double rate) {
        // default
    }

    public default void gauge(String metricName, double value) {
        // default
    }

    public default void gauge(String metricName, double value, double rate) {
        // default
    }

    public default void set(String metricName, String value) {
        // default
    }

    public default void set(String metricName, String value, double rate) {
        // default
    }

    public default void timing(String metricName, long value) {
        // default
    }

    public default void timing(String metricName, long value, double rate) {
        // default
    }

}
