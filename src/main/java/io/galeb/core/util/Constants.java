package io.galeb.core.util;

public final class Constants {

    public static final String TRUE  = Boolean.toString(true);

    public static final String FALSE = Boolean.toString(false);

    public static final String PROP_ENABLE_ACCESSLOG   = "io.galeb.accesslog";

    public static final String PROP_MAXCONN            = "io.galeb.maxConn";

    public static final String PROP_SCHEDULER_INTERVAL = "io.galeb.schedulerInterval";

    static {
        if (System.getProperty(PROP_ENABLE_ACCESSLOG)==null) {
            System.setProperty(PROP_ENABLE_ACCESSLOG, FALSE);
        }
        if (System.getProperty(PROP_MAXCONN)==null) {
            System.setProperty(PROP_MAXCONN, String.valueOf(1000));
        }
        if (System.getProperty(PROP_SCHEDULER_INTERVAL)==null) {
            System.setProperty(PROP_SCHEDULER_INTERVAL, String.valueOf(1000L));
        }
    }

    private Constants() {
        // static dictionary only
    }

}
