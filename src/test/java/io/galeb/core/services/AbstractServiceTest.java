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

import static org.assertj.core.api.Assertions.assertThat;
import io.galeb.core.cluster.ClusterEvents;
import io.galeb.core.cluster.DistributedMap;
import io.galeb.core.cluster.DistributedMapListener;
import io.galeb.core.cluster.DistributedMapStats;
import io.galeb.core.controller.BackendController;
import io.galeb.core.controller.BackendPoolController;
import io.galeb.core.controller.EntityController;
import io.galeb.core.controller.RuleController;
import io.galeb.core.controller.VirtualHostController;
import io.galeb.core.logging.impl.Log4j2Logger;
import io.galeb.core.mapreduce.MapReduce;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Farm;
import io.galeb.core.model.VirtualHost;
import io.galeb.core.statsd.StatsdClient;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AbstractServiceTest {

    static class ServiceImplemented extends AbstractService {
        //
    }

    static class FakeFarm extends Farm {
        private static final long serialVersionUID = 1L;
    }

    static class FakeMapReduce implements MapReduce {
        // Fake
    }

    static class FakeDistributedMapStats implements DistributedMapStats {
        @Override
        public String getStats() {
            return this.getClass().getName();
        }

        @Override
        public String toString() {
            return getStats();
        }
    }

    static class FakeDistributedMap implements DistributedMap<String, Entity> {
        // Fake

        @Override
        public ConcurrentMap<String, Entity> getMap(String key) {
            return null;
        }

        @Override
        public void registerListener(DistributedMapListener distributedMapListener) {
            // NULL
        }

        @Override
        public MapReduce getMapReduce() {
            return new FakeMapReduce();
        }

        @Override
        public DistributedMapStats getStats() {
            return new FakeDistributedMapStats();
        }
    }

    static class FakeClusterEvents implements ClusterEvents {
        // Fake
    }

    static class FakeStatsdClient implements StatsdClient {
        // Fake
    }

    @Inject
    private AbstractService serviceImplemented;

    private Farm farm;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                         .addClasses(
                                 ServiceImplemented.class,
                                 Log4j2Logger.class,
                                 FakeFarm.class,
                                 FakeDistributedMap.class,
                                 FakeClusterEvents.class,
                                 FakeStatsdClient.class)
                         .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void setUp() {
        farm = serviceImplemented.getFarm();
        farm.clear(BackendPool.class);
        farm.clear(VirtualHost.class);
    }

    @After
    public void clearDown() {
        farm.getEntityMap().clear();
    }

    @Test
    public void entityMapAtAbstractServiceIsNotNull() {
        assertThat(serviceImplemented.getFarm()).isNotNull();
    }

    @Test
    public void entityMapIsNotEmptyBeforeAbstractServicePreLaunch() {
        assertThat(serviceImplemented.getFarm().getEntityMap().isEmpty()).isTrue();
    }

    @Test
    public void entityMapIsNotEmptyAfterAbstractServicePreLaunch() {
        serviceImplemented.prelaunch();
        assertThat(serviceImplemented.getFarm().getEntityMap().isEmpty()).isFalse();
    }

    @Test
    public void entityMapHasFiveEntitiesAfterAbstractServicePreLaunch() {
        serviceImplemented.prelaunch();
        assertThat(serviceImplemented.getFarm().getEntityMap()).hasSize(5);
    }

    private String getKeyNameFrom(Class<?> klass) {
        return klass.getSimpleName().toLowerCase().replaceAll("controller", "");
    }

    private Map<String, EntityController> getEntityMapAfterPreLaunch() {
        serviceImplemented.prelaunch();
        final Map<String, EntityController> entities = serviceImplemented.getFarm().getEntityMap();
        return entities;
    }

    @Test
    public void entityMapHasBackendControllerAfterAbstractServicePreLaunch() {
        final Map<String, EntityController> entities = getEntityMapAfterPreLaunch();

        final EntityController anEntityInstance = entities.get(getKeyNameFrom(BackendController.class));
        assertThat(anEntityInstance).isInstanceOf(BackendController.class);
    }

    @Test
    public void entityMapHasBackendPoolControllerAfterAbstractServicePreLaunch() {
        final Map<String, EntityController> entities = getEntityMapAfterPreLaunch();

        final EntityController anEntityInstance = entities.get(getKeyNameFrom(BackendPoolController.class));
        assertThat(anEntityInstance).isInstanceOf(BackendPoolController.class);
    }

    @Test
    public void entityMapHasVirtualHostControllerAfterAbstractServicePreLaunch() {
        final Map<String, EntityController> entities = getEntityMapAfterPreLaunch();

        final EntityController anEntityInstance = entities.get(getKeyNameFrom(VirtualHostController.class));
        assertThat(anEntityInstance).isInstanceOf(VirtualHostController.class);
    }

    @Test
    public void entityMapHasRuleControllerAfterAbstractServicePreLaunch() {
        final Map<String, EntityController> entities = getEntityMapAfterPreLaunch();

        final EntityController anEntityInstance = entities.get(getKeyNameFrom(RuleController.class));
        assertThat(anEntityInstance).isInstanceOf(RuleController.class);
    }

}
