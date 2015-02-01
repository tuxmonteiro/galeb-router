package com.openvraas.core.logging;

public interface Logger {

    public enum Level {
        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    public void setSource(Object source);

    public void trace(String message);

    public void debug(String message);

    public void info(String message);

    public void warn(String message);

    public void error(String message);

    public void trace(Throwable throwable);

    public void debug(Throwable throwable);

    public void info(Throwable throwable);

    public void warn(Throwable throwable);

    public void error(Throwable throwable);

    public void log(String levelName, String message);

    public void log(String levelName, Throwable throwable);


}
