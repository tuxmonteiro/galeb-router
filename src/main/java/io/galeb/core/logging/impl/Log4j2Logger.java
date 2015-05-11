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

package io.galeb.core.logging.impl;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import io.galeb.core.logging.Logger;
import io.galeb.core.logging.annotation.LoggerSingletoneProducer;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.spi.ExtendedLogger;

@Default
public class Log4j2Logger implements Logger {

    private static final Log4j2Logger INSTANCE = new Log4j2Logger();

    private static final Map<String, Level> LOG_LEVELS = new HashMap<>();

    private static ExtendedLogger logger = LogManager.getContext().getLogger("io.galeb");

    static {
        for (Level level : EnumSet.allOf(Level.class)) {
            LOG_LEVELS.put(level.toString(), level);
        }
    }

    private Log4j2Logger() {
    }

    @Produces @LoggerSingletoneProducer
    public static Logger getInstance() {
        return INSTANCE;
    }

    @Override
    public void trace(String message) {
        logger.trace(message);
    }

    @Override
    public void debug(String message) {
        logger.debug(message);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warn(String message) {
        logger.warn(message);
    }

    @Override
    public void error(String message) {
        logger.error(message);
    }

    @Override
    public void trace(Throwable throwable) {
        trace(getStackTrace(throwable));
    }

    @Override
    public void debug(Throwable throwable) {
        debug(getStackTrace(throwable));
    }

    @Override
    public void info(Throwable throwable) {
        info(getStackTrace(throwable));
    }

    @Override
    public void warn(Throwable throwable) {
        warn(getStackTrace(throwable));
    }

    @Override
    public void error(Throwable throwable) {
        error(getStackTrace(throwable));
    }

    @Override
    public void log(String levelName, String message) {
        if (levelName == null || message == null) {
            return;
        }
        if (LOG_LEVELS.get(levelName)==null) {
            return;
        }
        if (Level.TRACE.toString().equals(levelName)) {
            trace(message);
            return;
        }
        if (Level.DEBUG.toString().equals(levelName)) {
            debug(message);
            return;
        }
        if (Level.INFO.toString().equals(levelName)) {
            info(message);
            return;
        }
        if (Level.WARN.toString().equals(levelName)) {
            warn(message);
            return;
        }
        if (Level.ERROR.toString().equals(levelName)) {
            error(message);
            return;
        }
    }

    @Override
    public void log(String levelName, Throwable throwable) {
        log(levelName, getStackTrace(throwable));
    }

}
