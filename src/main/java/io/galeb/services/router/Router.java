package io.galeb.services.router;

import io.galeb.core.controller.EntityController.Action;
import io.galeb.core.json.JsonObject;
import io.galeb.core.metrics.CounterConnections;
import io.galeb.core.metrics.CounterConnections.Data;
import io.galeb.core.metrics.CounterConnectionsListener;
import io.galeb.core.model.Metrics;
import io.galeb.core.services.AbstractService;
import io.galeb.undertow.router.RouterApplication;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

public class Router extends AbstractService implements CounterConnectionsListener {

    private static final String PROP_ROUTER_PREFIX    = "io.galeb.router.";

    private static final String PROP_ROUTER_PORT      = PROP_ROUTER_PREFIX+"port";

    private static final String PROP_ROUTER_IOTHREADS = PROP_ROUTER_PREFIX+"iothread";

    private static final String PROP_ROUTER_MAXCONN   = PROP_ROUTER_PREFIX+"maxConn";

    public static final int     DEFAULT_PORT          = 8080;

    static {
        if (System.getProperty(PROP_ROUTER_PORT)==null) {
            System.setProperty(PROP_ROUTER_PORT, Integer.toString(DEFAULT_PORT));
        }
        if (System.getProperty(PROP_ROUTER_IOTHREADS)==null) {
            System.setProperty(PROP_ROUTER_IOTHREADS, String.valueOf(Runtime.getRuntime().availableProcessors()));
        }
        if (System.getProperty(PROP_ROUTER_MAXCONN)==null) {
            System.setProperty(PROP_ROUTER_MAXCONN, Integer.toString(100));
        }
    }

    @PostConstruct
    protected void init() {

        CounterConnections.registerListener(this);

        super.prelaunch();

        final int port = Integer.parseInt(System.getProperty(PROP_ROUTER_PORT));
        final String iothreads = System.getProperty(PROP_ROUTER_IOTHREADS);

        final Map<String, String> options = new HashMap<>();
        options.put("IoThreads", iothreads);

        new RouterApplication().setHost("0.0.0.0")
                               .setPort(port)
                               .setOptions(options)
                               .setFarm(farm)
                               .start();

        logger.debug(String.format("[0.0.0.0:%d] ready", port));
    }

    public Router() {
        super();
    }

    @Override
    public void handleController(JsonObject json, Action action) {
        // future
    }

    @Override
    public void hasNewData() {
        final Data data = CounterConnections.poolData();
        final Metrics metrics = (Metrics) new Metrics().setId(data.getKey()).getProperties().put(Metrics.PROP_METRICS_TOTAL, data.getTotal());
        eventbus.sendMetrics(metrics);
    }

}
