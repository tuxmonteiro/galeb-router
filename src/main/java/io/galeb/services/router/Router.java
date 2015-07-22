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

import org.quartz.SchedulerException;

import io.galeb.core.services.AbstractService;
import io.galeb.services.router.sched.BackendUpdaterJob;
import io.galeb.services.router.sched.QuartzScheduler;
import io.galeb.undertow.router.RouterApplication;

public class Router extends AbstractService {

    private static final String PROP_ROUTER_PREFIX    = Router.class.getPackage().getName()+".";

    private static final String PROP_ROUTER_PORT      = PROP_ROUTER_PREFIX+"port";

    private static final String PROP_ROUTER_IOTHREADS = PROP_ROUTER_PREFIX+"iothread";

    public static final int     DEFAULT_PORT          = 8080;

    private boolean schedulerStarted = false;

    static {
        if (System.getProperty(PROP_ROUTER_PORT)==null) {
            System.setProperty(PROP_ROUTER_PORT, Integer.toString(DEFAULT_PORT));
        }
        if (System.getProperty(PROP_ROUTER_IOTHREADS)==null) {
            System.setProperty(PROP_ROUTER_IOTHREADS, String.valueOf(Runtime.getRuntime().availableProcessors()));
        }
    }

    @PostConstruct
    public void init() {

        super.prelaunch();

        try {
            startSchedulers();
        } catch (final SchedulerException e) {
            logger.error(e);
        }

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

    private void startSchedulers() throws SchedulerException {
        if (schedulerStarted) {
            return;
        }
        final long interval = Long.parseLong(System.getProperty(PROP_SCHEDULER_INTERVAL.toString(), PROP_SCHEDULER_INTERVAL.def()));
        new QuartzScheduler(farm, statsdClient, distributedMap, clusterEvents, logger)
                        .startPeriodicJob(BackendUpdaterJob.class, interval);
        logger.info("scheduler started");
        schedulerStarted = true;
    }

}
