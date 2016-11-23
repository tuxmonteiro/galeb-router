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

package io.galeb.services.router;

import static io.galeb.core.services.ProcessorScheduler.PROP_PROCESSOR_INTERVAL;
import static io.galeb.core.util.Constants.SysProp.PROP_SCHEDULER_INTERVAL;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.cache.Cache;

import io.galeb.core.cluster.ignite.IgniteCacheFactory;
import io.galeb.core.cluster.ignite.IgniteClusterLocker;
import io.galeb.core.model.*;
import io.galeb.services.router.api.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.SchedulerException;

import io.galeb.core.services.AbstractService;
import io.galeb.services.router.sched.BackendUpdaterJob;
import io.galeb.services.router.sched.QuartzScheduler;
import io.galeb.undertow.router.RouterApplication;

public class Router extends AbstractService {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String PROP_ROUTER_PREFIX       = Router.class.getPackage().getName()+".";

    private static final String PROP_ROUTER_BIND         = PROP_ROUTER_PREFIX + "bind";

    private static final String PROP_ROUTER_PORT         = PROP_ROUTER_PREFIX + "port";

    private static final String PROP_ROUTER_IOTHREADS    = PROP_ROUTER_PREFIX + "iothread";

    private static final String PROP_ROUTER_WORK_THREADS = PROP_ROUTER_PREFIX + "workers";

    private static final String PROP_ROUTER_MAX_WORKS    = PROP_ROUTER_PREFIX + "max_workers";

    private static final String PROP_ROUTER_BACKLOG      = PROP_ROUTER_PREFIX + "backlog";

    private static final String PROP_ROUTER_IDLETIMEOUT  = PROP_ROUTER_PREFIX + "idleTimeout";

    private static final String PROP_DELAY_ON_BOOT       = PROP_ROUTER_PREFIX + "delayOnBoot";

    public static final int     DEFAULT_PORT             = 8080;

    static {
        Farm.STATIC_PROPERTIES.put(Farm.MAX_REQUEST_TIME_FARM_PROP, System.getProperty(PROP_ROUTER_IDLETIMEOUT, "0"));
        Farm.STATIC_PROPERTIES.put(Farm.FORCE_CHANGE_STATUS_FARM_PROP, System.getProperty(Farm.FORCE_CHANGE_STATUS_FARM_PROP, String.valueOf(false)));
    }

    private boolean schedulerStarted = false;

    @PostConstruct
    public void init() {
        cacheFactory = IgniteCacheFactory.getInstance()
                                            .setFarm(farm)
                                            .listeningPutEvent()
                                            .listeningRemoveEvent()
                                            .start();
        clusterLocker = IgniteClusterLocker.getInstance().start();


        final long delayOnBoot = Long.parseLong(System.getProperty(PROP_DELAY_ON_BOOT, "5000"));

        syncMaps(delayOnBoot);

        final String bind = System.getProperty(PROP_ROUTER_BIND, "0.0.0.0");
        final int port = Integer.parseInt(System.getProperty(PROP_ROUTER_PORT, Integer.toString(DEFAULT_PORT)));
        final String iothreads = System.getProperty(PROP_ROUTER_IOTHREADS, String.valueOf(Runtime.getRuntime().availableProcessors()));
        final String workers = System.getProperty(PROP_ROUTER_WORK_THREADS, String.valueOf(Runtime.getRuntime().availableProcessors() * 8));
        final String maxWorks = System.getProperty(PROP_ROUTER_MAX_WORKS, workers);
        final String backLog = System.getProperty(PROP_ROUTER_BACKLOG, "1000");
        final String idleTimeout = System.getProperty(PROP_ROUTER_IDLETIMEOUT, "0");

        final Map<String, String> options = new HashMap<>();
        options.put("IoThreads", iothreads);
        options.put("workers", workers);
        options.put("max_workers", maxWorks);
        options.put("backlog", backLog);
        options.put("idleTimeout", idleTimeout);

        new RouterApplication().setHost(bind)
                               .setPort(port)
                               .setOptions(options)
                               .setFarm(farm)
                               .start();

        try {
            startSchedulers();
        } catch (final SchedulerException e) {
            LOGGER.error(e);
        }


        LOGGER.info(String.format("Router [%s:%d] ready", bind, port));
    }

    private void syncMaps(long delayOnBoot) {
        try {
            Thread.sleep(delayOnBoot);
            preload();
            super.startProcessorScheduler();
            int interval = Integer.parseInt(System.getProperty(PROP_PROCESSOR_INTERVAL, "1"));
            Thread.sleep(interval * 1000L);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
    }

    public Router() {
        super();
    }

    private void startSchedulers() throws SchedulerException {
        if (schedulerStarted) {
            return;
        }
        final long interval = Long.parseLong(System.getProperty(PROP_SCHEDULER_INTERVAL.toString(), PROP_SCHEDULER_INTERVAL.def()));
        new QuartzScheduler(farm, statsdClient)
                        .startPeriodicJob(BackendUpdaterJob.class, interval);
        LOGGER.info("scheduler started");
        schedulerStarted = true;
    }

    private void preload() {
        Arrays.asList(Backend.class, BackendPool.class, Rule.class, VirtualHost.class).stream()
                .forEach(clazz -> {
                    Cache<String, String> cache = cacheFactory.getCache(clazz.getName());
                    if (cache != null) {
                        cache.forEach(entry -> {
                            String json = entry.getValue();
                            entityAdd(json, clazz);
                            LOGGER.warn("Loaded entity: " + json);
                        });
                    }
                });
    }
}
