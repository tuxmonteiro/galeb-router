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

package io.galeb.core.starter;

import io.galeb.core.cdi.WeldContext;

public abstract class AbstractStarter {

    private static final String PROP_JAVA_UTIL_LOGGING_MANAGER  = "java.util.logging.manager";
    private static final String PROP_ORG_JBOSS_LOGGING_PROVIDER = "org.jboss.logging.provider";

    static {
        if (System.getProperty(PROP_ORG_JBOSS_LOGGING_PROVIDER)==null) {
            System.setProperty(PROP_ORG_JBOSS_LOGGING_PROVIDER, "log4j2");
        }
        if (System.getProperty(PROP_JAVA_UTIL_LOGGING_MANAGER)==null) {
            System.setProperty(PROP_JAVA_UTIL_LOGGING_MANAGER, "org.apache.logging.log4j.jul.LogManager");
        }
    }

    protected AbstractStarter() {
        //
    }

    protected static void loadService(Class<?> clazz) {
        WeldContext.INSTANCE.getBean(clazz);
    }

}
