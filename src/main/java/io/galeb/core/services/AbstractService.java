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

package io.galeb.core.services;

import static io.galeb.core.util.Constants.SysProp.PROP_SCHEDULER_INTERVAL;
import io.galeb.core.controller.BackendController;
import io.galeb.core.controller.BackendPoolController;
import io.galeb.core.controller.EntityController;
import io.galeb.core.controller.EntityController.Action;
import io.galeb.core.controller.RuleController;
import io.galeb.core.controller.VirtualHostController;
import io.galeb.core.eventbus.Event;
import io.galeb.core.eventbus.EventBusListener;
import io.galeb.core.eventbus.IEventBus;
import io.galeb.core.json.JsonObject;
import io.galeb.core.logging.Logger;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Farm;
import io.galeb.core.sched.BackendPoolUpdaterJob;
import io.galeb.core.sched.BackendUpdaterJob;
import io.galeb.core.sched.QuartzScheduler;

import java.util.Map;

import javax.inject.Inject;

import org.quartz.SchedulerException;

public abstract class AbstractService implements EventBusListener {

    @Inject
    protected Farm farm;

    @Inject
    protected IEventBus eventbus;

    @Inject
    protected Logger logger;

    protected QuartzScheduler scheduler;

    public AbstractService() {
        super();
    }

    protected void prelaunch() {
        eventbus.setEventBusListener(this).start();
        registerControllers();
        try {
            startSchedulers();
        } catch (final SchedulerException e) {
            logger.error(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                eventbus.stop();
            }
        });
    }

    protected void registerControllers() {

        final Map<String, EntityController> entityMap = farm.getEntityMap();

        entityMap.put(getControllerName(BackendController.class),
                new BackendController(farm));
        entityMap.put(getControllerName(BackendPoolController.class),
                new BackendPoolController(farm));
        entityMap.put(getControllerName(RuleController.class),
                new RuleController(farm));
        entityMap.put(getControllerName(VirtualHostController.class),
                new VirtualHostController(farm));

    }

    protected void startSchedulers() throws SchedulerException {
        final long interval = Long.parseLong(System.getProperty(PROP_SCHEDULER_INTERVAL.toString(), PROP_SCHEDULER_INTERVAL.def()));
        scheduler = new QuartzScheduler(farm, eventbus, logger)
                        .startPeriodicJob(BackendPoolUpdaterJob.class, interval)
                        .startPeriodicJob(BackendUpdaterJob.class, interval);
    }

    private String getControllerName(Class<?> clazz) {
        return clazz.getSimpleName().toLowerCase().replace("controller", "");
    }

    public Farm getFarm() {
        return farm;
    }

    @Override
    public IEventBus getEventBus() {
        return eventbus;
    }

    @Override
    public void onEvent(Event event) throws RuntimeException {

        final JsonObject json = event.getData();

        final Entity entity = (Entity) json.instanceOf(Entity.class);
        final String entityType = entity.getEntityType();

        EntityController entityController = farm.getEntityMap().get(entityType);
        if (entityController==null) {
            entityController = EntityController.NULL;
            logger.error("EntityController is NULL");
        }

        final Object eventType = event.getType();

        if (eventType instanceof Action) {
            final Action action = (Action) eventType;

            try {
                switch (action) {
                    case ADD:
                        entityController.add(json);
                        break;
                    case DEL:
                        entityController.del(json);
                        break;
                    case DEL_ALL:
                        entityController.delAll();
                        break;
                    case CHANGE:
                        entityController.change(json);
                        break;
                    default:
                        throw new RuntimeException("Action unknown");
                }
            } catch (final Exception e) {
                logger.debug(e);
            }
        }

    }

}
