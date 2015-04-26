package io.galeb.core.starter;

public abstract class Properties {

    private static final String PROP_JAVA_UTIL_LOGGING_MANAGER  = "java.util.logging.manager";
    private static final String PROP_ORG_JBOSS_LOGGING_PROVIDER = "org.jboss.logging.provider";
    private static final String PROP_HAZELCAST_LOGGING_TYPE     = "hazelcast.logging.type";

    static {
        if (System.getProperty(PROP_HAZELCAST_LOGGING_TYPE)==null) {
            System.setProperty(PROP_HAZELCAST_LOGGING_TYPE, "log4j2");
        }
        if (System.getProperty(PROP_ORG_JBOSS_LOGGING_PROVIDER)==null) {
            System.setProperty(PROP_ORG_JBOSS_LOGGING_PROVIDER, "log4j2");
        }
        if (System.getProperty(PROP_JAVA_UTIL_LOGGING_MANAGER)==null) {
            System.setProperty(PROP_JAVA_UTIL_LOGGING_MANAGER, "org.apache.logging.log4j.jul.LogManager");
        }
    }

    protected Properties() {
        //
    }

}
