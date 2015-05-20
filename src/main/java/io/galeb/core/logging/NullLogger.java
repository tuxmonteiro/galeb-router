package io.galeb.core.logging;

import javax.enterprise.inject.Alternative;

@Alternative
public class NullLogger implements Logger {

    @Override
    public void trace(String message) {
        // NULL
    }

    @Override
    public void debug(String message) {
        // NULL
    }

    @Override
    public void info(String message) {
        // NULL
    }

    @Override
    public void warn(String message) {
        // NULL
    }

    @Override
    public void error(String message) {
        // NULL
    }

    @Override
    public void trace(Throwable throwable) {
        // NULL
    }

    @Override
    public void debug(Throwable throwable) {
        // NULL
    }

    @Override
    public void info(Throwable throwable) {
        // NULL
    }

    @Override
    public void warn(Throwable throwable) {
        // NULL
    }

    @Override
    public void error(Throwable throwable) {
        // NULL
    }

    @Override
    public void log(String levelName, String message) {
        // NULL
    }

    @Override
    public void log(String levelName, Throwable throwable) {
        // NULL
    }

}
