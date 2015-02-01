package com.openvraas.core.logging.impl;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.spi.ExtendedLogger;

import com.openvraas.core.logging.Logger;
import com.openvraas.core.logging.annotation.LoggerSingletoneProducer;

@Default
public class Log4j2Logger implements Logger {

    private static final Log4j2Logger INSTANCE = new Log4j2Logger();

    private static final String FQCN = Log4j2Logger.class.getName();

    private static final Map<String, Level> LOG_LEVELS = new HashMap<>();

    private static ExtendedLogger logger = LogManager.getContext().getLogger(FQCN);

    private Object source = null;

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

    private String addSourceToMessage(String message) {
        if (source!=null) {
            message = "<"+source.toString()+"> "+message;
        }
        return message;
    }

    @Override
    public void setSource(Object source) {
        this.source = source;
    }

    @Override
    public void trace(String message) {
        logger.trace(addSourceToMessage(message));
    }

    @Override
    public void debug(String message) {
        logger.debug(addSourceToMessage(message));
    }

    @Override
    public void info(String message) {
        logger.info(addSourceToMessage(message));
    }

    @Override
    public void warn(String message) {
        logger.warn(addSourceToMessage(message));
    }

    @Override
    public void error(String message) {
        logger.error(addSourceToMessage(message));
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
