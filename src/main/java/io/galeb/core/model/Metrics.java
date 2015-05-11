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

package io.galeb.core.model;

import io.galeb.core.json.JsonObject;

public class Metrics extends Entity {

    private static final long serialVersionUID = 4453537887347058918L;

    public static final String METRICS_QUEUE = "METRICS_QUEUE";

    public static final String PROP_METRICS_TOTAL = "METRICS_TOTAL";

    public static final String PROP_STATUSCODE = "status";

    public static final String PROP_HTTPCODE_PREFIX = "httpCode";

    public static final String PROP_REQUESTTIME = "requestTime";

    public static final String PROP_REQUESTTIME_AVG = "requestTimeAvg";

    public Metrics() {
        // Default
    }

    public Metrics(Metrics metrics) {
        this();
        final String metricsStr = JsonObject.toJsonString(metrics);
        final Metrics newMetrics = (Metrics) JsonObject.fromJson(metricsStr, Metrics.class);

        setId(newMetrics.getId());
        setParentId(newMetrics.getParentId());
        setProperties(newMetrics.getProperties());
        updateHash();
    }

    public enum Operation {
        SUM,
        AVG
    }

    public Metrics aggregationProperty(final Metrics metrics, String propName, Operation oper) {

        final Object propValue = metrics.getProperty(propName);
        final Object myPropValue = getProperty(propName);
        Integer valueInt = 0;

        if (propValue!=null) {
            if (myPropValue==null) {
                putProperty(propName, propValue);
                return this;
            }

            try {
                switch (oper) {
                    case SUM:
                        valueInt = (Integer) myPropValue;
                        valueInt += (Integer) propValue;
                        break;
                    case AVG:
                        valueInt = (Integer) myPropValue;
                        valueInt = (valueInt + (Integer) propValue) / 2;
                    default:
                        break;
                }
                putProperty(propName, valueInt);
            } catch (final ClassCastException ignore) {
                // ignore aggregation
            }
        }
        return this;
    }

}
