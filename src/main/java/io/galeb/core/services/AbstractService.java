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

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Inject;

import io.galeb.core.cluster.ClusterEvents;
import io.galeb.core.cluster.ClusterListener;
import io.galeb.core.cluster.DistributedMap;
import io.galeb.core.cluster.DistributedMapListener;
import io.galeb.core.controller.BackendController;
import io.galeb.core.controller.BackendPoolController;
import io.galeb.core.controller.EntityController;
import io.galeb.core.controller.FarmController;
import io.galeb.core.controller.RuleController;
import io.galeb.core.controller.VirtualHostController;
import io.galeb.core.logging.Logger;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Farm;
import io.galeb.core.model.Rule;
import io.galeb.core.model.VirtualHost;
import io.galeb.core.statsd.StatsdClient;

public abstract class AbstractService implements DistributedMapListener,
                                                 ClusterListener {

    public static final String LOGGER          = "logger";
    public static final String FARM            = "farm";
    public static final String DISTRIBUTEDMAP  = "distributedMap";
    public static final String STATSD          = "statsd";
    public static final String CLUSTER_EVENTS  = "clusterEvents";
    public static final String INTERVAL        = "interval";

    @Inject
    protected Farm farm;

    @Inject
    protected DistributedMap<String, Entity> distributedMap;

    @Inject
    protected Logger logger;

    @Inject
    protected ClusterEvents clusterEvents;

    @Inject
    protected StatsdClient statsdClient;

    private boolean clusterListenerRegistered = false;

    public AbstractService() {
        super();
    }

    private void entityAdd(Entity entity) {
        EntityController entityController = farm.getEntityMap().get(entity.getEntityType());
        try {
            entityController.add(entity.copy());
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private void registerCluster() {
        clusterEvents.registerListener(this);
        if (clusterEvents.isReady() && !clusterListenerRegistered) {
            onClusterReady();
        }
    }

    protected void prelaunch() {
        registerControllers();
        registerCluster();
    }

    protected void registerControllers() {

        final Map<String, EntityController> entityMap = farm.getEntityMap();

        entityMap.put(EntityController.getControllerName(BackendController.class),
                new BackendController(farm));
        entityMap.put(EntityController.getControllerName(BackendPoolController.class),
                new BackendPoolController(farm));
        entityMap.put(EntityController.getControllerName(RuleController.class),
                new RuleController(farm));
        entityMap.put(EntityController.getControllerName(VirtualHostController.class),
                new VirtualHostController(farm));
        entityMap.put(EntityController.getControllerName(FarmController.class),
                new FarmController(farm, entityMap));

    }

    public Farm getFarm() {
        return farm;
    }

    public Logger getLogger() {
        return logger;
    }

    public DistributedMap<String, Entity> getDistributedMap() {
        return distributedMap;
    }

    @Override
    public void entryAdded(Entity entity) {
        logger.debug("entryAdded: "+entity.getId()+" ("+entity.getEntityType()+")");
        entityAdd(entity);
        showStatistic();
    }

    @Override
    public void entryRemoved(Entity entity) {
        logger.debug("entryRemoved: "+entity.getId()+" ("+entity.getEntityType()+")");
        EntityController entityController = farm.getEntityMap().get(entity.getEntityType());
        try {
            entityController.del(entity.copy());
        } catch (Exception e) {
            logger.error(e);
        }
        showStatistic();
    }

    @Override
    public void entryUpdated(Entity entity) {
        logger.debug("entryUpdated: "+entity.getId()+" ("+entity.getEntityType()+")");
        EntityController entityController = farm.getEntityMap().get(entity.getEntityType());
        try {
            entityController.change(entity.copy());
        } catch (Exception e) {
            logger.error(e);
        }
        showStatistic();
    }

    @Override
    public void mapCleared(String mapName) {
        logger.debug("mapCleared: "+mapName);
        EntityController entityController = farm.getEntityMap().get(mapName.toLowerCase());
        try {
            entityController.delAll();
        } catch (Exception e) {
            logger.error(e);
        }
        showStatistic();
    }

    @Override
    public void entryEvicted(Entity entity) {
        logger.debug("entryEvicted: "+entity.getId()+" ("+entity.getEntityType()+")");
        entryRemoved(entity);
        showStatistic();
    }

    @Override
    public void mapEvicted(String mapName) {
        logger.debug("mapEvicted: "+mapName);
        mapCleared(mapName);
        showStatistic();
    }

    @Override
    public void showStatistic() {
        if (logger.isDebugEnabled()) {
            logger.debug(distributedMap.getStats().toString());
        }
    }

    @Override
    public void onClusterReady() {
        logger.info("== Cluster ready");
        distributedMap.registerListener(this);
        Arrays.asList(Backend.class, BackendPool.class, Rule.class, VirtualHost.class).stream()
            .forEach(clazz -> {
                ConcurrentMap<String, Entity> map = distributedMap.getMap(clazz.getName());
                map.forEach( (key, entity) -> {
                    entityAdd(entity);
                });
            });
        clusterListenerRegistered = true;
    }

}
