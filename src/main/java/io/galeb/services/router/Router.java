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

import static io.galeb.core.util.Constants.SysProp.PROP_SCHEDULER_INTERVAL;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import io.galeb.core.cluster.ignite.IgniteCacheFactory;
import io.galeb.core.cluster.ignite.IgniteClusterLocker;
import org.quartz.SchedulerException;

import io.galeb.core.services.AbstractService;
import io.galeb.services.router.sched.BackendUpdaterJob;
import io.galeb.services.router.sched.QuartzScheduler;
import io.galeb.undertow.router.RouterApplication;

public class Router extends AbstractService {

    private static final String PROP_ROUTER_PREFIX       = Router.class.getPackage().getName()+".";

    private static final String PROP_ROUTER_PORT         = PROP_ROUTER_PREFIX+"port";

    private static final String PROP_ROUTER_IOTHREADS    = PROP_ROUTER_PREFIX+"iothread";

    private static final String PROP_ROUTER_WORK_THREADS = PROP_ROUTER_PREFIX+"workers";

    private static final String PROP_ROUTER_MAX_WORKS    = PROP_ROUTER_PREFIX+"max_workers";

    private static final String PROP_ROUTER_BACKLOG      = PROP_ROUTER_PREFIX+"backlog";

    private static final String PROP_ROUTER_IDLETIMEOUT  = PROP_ROUTER_PREFIX+"idleTimeout";

    public static final int     DEFAULT_PORT             = 8080;


    private boolean schedulerStarted = false;

    static {
        if (System.getProperty(PROP_ROUTER_PORT)==null) {
            System.setProperty(PROP_ROUTER_PORT, Integer.toString(DEFAULT_PORT));
        }
        if (System.getProperty(PROP_ROUTER_IOTHREADS)==null) {
            System.setProperty(PROP_ROUTER_IOTHREADS, String.valueOf(Runtime.getRuntime().availableProcessors()));
        }
        if (System.getProperty(PROP_ROUTER_WORK_THREADS)==null) {
            System.setProperty(PROP_ROUTER_WORK_THREADS, String.valueOf(Runtime.getRuntime().availableProcessors()*8));
        }
        if (System.getProperty(PROP_ROUTER_MAX_WORKS)==null) {
            System.setProperty(PROP_ROUTER_MAX_WORKS, System.getProperty(PROP_ROUTER_WORK_THREADS));
        }
        if (System.getProperty(PROP_ROUTER_BACKLOG)==null) {
            System.setProperty(PROP_ROUTER_BACKLOG, "1000");
        }
        if (System.getProperty(PROP_ROUTER_IDLETIMEOUT)==null) {
            System.setProperty(PROP_ROUTER_IDLETIMEOUT, "60");
        }
    }

    @PostConstruct
    public void init() {
        cacheFactory = IgniteCacheFactory.getInstance(this)
                                            .setFarm(farm)
                                            .listeningPutEvent()
                                            .listeningRemoveEvent()
                                            .listeningReadEvent()
                                            .start();
        clusterLocker = IgniteClusterLocker.INSTANCE;
        cacheFactory.setLogger(logger);
        clusterLocker.setLogger(logger);

        super.startProcessorScheduler();

        try {
            startSchedulers();
        } catch (final SchedulerException e) {
            logger.error(e);
        }

        final int port = Integer.parseInt(System.getProperty(PROP_ROUTER_PORT));
        final String iothreads = System.getProperty(PROP_ROUTER_IOTHREADS);
        final String workers = System.getProperty(PROP_ROUTER_WORK_THREADS);
        final String maxWorks = System.getProperty(PROP_ROUTER_MAX_WORKS);
        final String backLog = System.getProperty(PROP_ROUTER_BACKLOG);
        final String idleTimeout = System.getProperty(PROP_ROUTER_IDLETIMEOUT);

        final Map<String, String> options = new HashMap<>();
        options.put("IoThreads", iothreads);
        options.put("workers", workers);
        options.put("max_workers", maxWorks);
        options.put("backlog", backLog);
        options.put("idleTimeout", idleTimeout);

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

    private void startSchedulers() throws SchedulerException {
        if (schedulerStarted) {
            return;
        }
        final long interval = Long.parseLong(System.getProperty(PROP_SCHEDULER_INTERVAL.toString(), PROP_SCHEDULER_INTERVAL.def()));
        new QuartzScheduler(farm, statsdClient, logger)
                        .startPeriodicJob(BackendUpdaterJob.class, interval);
        logger.info("scheduler started");
        schedulerStarted = true;
    }

    @Override
    public void onClusterRead(String json, String cacheName) {
        try {
            entityAdd(json, Class.forName(cacheName));
        } catch (ClassNotFoundException e) {
            logger.error(e);
        }
    }
}
