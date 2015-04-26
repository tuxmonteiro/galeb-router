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
