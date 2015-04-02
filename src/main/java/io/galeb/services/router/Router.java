package io.galeb.services.router;

import io.galeb.services.AbstractService;
import io.galeb.undertow.router.RouterApplication;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

public class Router extends AbstractService {

    private static final String PROP_ROUTER_PREFIX    = "io.galeb.router.";

    private static final String PROP_ROUTER_PORT      = PROP_ROUTER_PREFIX+"port";

    private static final String PROP_ROUTER_IOTHREADS = PROP_ROUTER_PREFIX+"iothread";

    private static final String PROP_ROUTER_METRICS   = PROP_ROUTER_PREFIX+"enableMetrics";

    private static final String PROP_ROUTER_MAXCONN   = PROP_ROUTER_PREFIX+"maxConn";

    static {
        if (System.getProperty(PROP_ROUTER_PORT)==null) {
            System.setProperty(PROP_ROUTER_PORT, "8080");
        }
        if (System.getProperty(PROP_ROUTER_IOTHREADS)==null) {
            System.setProperty(PROP_ROUTER_IOTHREADS, String.valueOf(Runtime.getRuntime().availableProcessors()));
        }
        if (System.getProperty(PROP_ROUTER_METRICS)==null) {
            System.setProperty(PROP_ROUTER_METRICS, "false");
        }
        if (System.getProperty(PROP_ROUTER_MAXCONN)==null) {
            System.setProperty(PROP_ROUTER_MAXCONN, "100");
        }

    }

    @PostConstruct
    protected void init() {

        super.prelaunch();

        int port = Integer.parseInt(System.getProperty(PROP_ROUTER_PORT));
        String iothreads = System.getProperty(PROP_ROUTER_IOTHREADS);

        final Map<String, String> options = new HashMap<>();
        options.put("IoThreads", iothreads);
        options.put("EnableMetrics", !"false".equals(System.getProperty(PROP_ROUTER_METRICS)) ? "true" : "false");

        new RouterApplication().setHost("0.0.0.0")
                               .setPort(port)
                               .setOptions(options)
                               .setFarm(farm)
                               .start();

        onLog("DEBUG", "[0.0.0.0:"+String.valueOf(port)+"] ready");
    }

    public Router() {
        super();
    }

}
