package io.galeb.core.services;

import static org.assertj.core.api.Assertions.assertThat;
import io.galeb.core.controller.BackendController;
import io.galeb.core.controller.BackendPoolController;
import io.galeb.core.controller.EntityController;
import io.galeb.core.controller.EntityController.Action;
import io.galeb.core.controller.RuleController;
import io.galeb.core.controller.VirtualHostController;
import io.galeb.core.eventbus.Event;
import io.galeb.core.eventbus.EventBusListener;
import io.galeb.core.eventbus.IEventBus;
import io.galeb.core.eventbus.NullEventBus;
import io.galeb.core.logging.impl.Log4j2Logger;
import io.galeb.core.mapreduce.MapReduce;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Farm;
import io.galeb.core.model.Metrics;
import io.galeb.core.model.Rule;
import io.galeb.core.model.VirtualHost;
import io.galeb.core.queue.QueueManager;

import java.util.Collections;
import java.util.Map;

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

    static class FakeEventBus implements IEventBus {
        @Override
        public void publishEntity(Entity entity, String entityType,
                Action action) {
        }

        @Override
        public void onRequestMetrics(Metrics metrics) {
            // NULL
        }

        @Override
        public void onConnectionsMetrics(Metrics metrics) {
            // NULL
        }

        @Override
        public IEventBus setEventBusListener(EventBusListener eventBusListener) {
            return this;
        }

        @Override
        public void start() {
            // NULL
        }

        @Override
        public MapReduce getMapReduce() {
            return NullEventBus.NULL_MAP_REDUCE;
        }

        @Override
        public QueueManager getQueueManager() {
            return QueueManager.NULL;
        }
    }

    static class FakeMapReduce implements MapReduce {
        @Override
        public MapReduce setTimeOut(Long timeOut) {
            return this;
        }

        @Override
        public Long getTimeOut() {
            return -1L;
        }

        @Override
        public void addMetrics(Metrics metrics) {
            // NULL
        }

        @Override
        public boolean contains(String backendId) {
            return false;
        }

        @Override
        public Map<String, Integer> reduce() {
            return Collections.emptyMap();
        }
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
                                 FakeEventBus.class,
                                 FakeFarm.class,
                                 FakeMapReduce.class)
                         .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void setUp() {
        farm = serviceImplemented.getFarm();
        farm.clearBackendPool();
        farm.clearVirtualHosts();
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
    public void entityMapHasFourEntitiesAfterAbstractServicePreLaunch() {
        serviceImplemented.prelaunch();
        assertThat(serviceImplemented.getFarm().getEntityMap()).hasSize(4);
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

    @Test(expected=UnsupportedOperationException.class)
    public void handleControllerAtAbstractServiceRaiseException(){
        serviceImplemented.handleController(null, null);
    }

    private Event makeEvent(Action action, Entity entity) {
        final String id = entity.getClass().getSimpleName().toLowerCase()+"Test";

        entity.setId(id);
        final Event event = new Event(action, entity);

        serviceImplemented.prelaunch();

        return event;
    }

    @Test
    public void onEventAddBackendTest() {
        final Entity entity = new Backend();

        final Entity parentEntity = new BackendPool();
        final String parentId = parentEntity.getClass().getSimpleName().toLowerCase()+"Test";

        parentEntity.setParentId(parentId);
        entity.setParentId(parentId);

        serviceImplemented.onEvent(makeEvent(Action.ADD, parentEntity));
        serviceImplemented.onEvent(makeEvent(Action.ADD, entity));
        assertThat(farm.getBackends()).extracting("id").contains(entity.getId());
    }

    @Test
    public void onEventAddBackendPoolTest() {
        final Entity entity = new BackendPool();

        serviceImplemented.onEvent(makeEvent(Action.ADD, entity));
        assertThat(farm.getBackendPools()).extracting("id").contains(entity.getId());
    }

    @Test
    public void onEventAddVirtualhostTest() {
        final Entity entity = new VirtualHost();

        serviceImplemented.onEvent(makeEvent(Action.ADD, entity));
        assertThat(farm.getVirtualHosts()).extracting("id").contains(entity.getId());
    }

    @Test
    public void onEventAddRuleTest() {
        final Entity entity = new Rule();

        final Entity parentEntity = new VirtualHost();
        final String parentId = parentEntity.getClass().getSimpleName().toLowerCase()+"Test";

        parentEntity.setParentId(parentId);
        entity.setParentId(parentId);

        serviceImplemented.onEvent(makeEvent(Action.ADD, parentEntity));
        serviceImplemented.onEvent(makeEvent(Action.ADD, entity));
        assertThat(farm.getRules()).extracting("id").contains(entity.getId());
    }

    @Test
    public void onEventDelBackendTest() {
        final Entity entity = new Backend();
        final Entity parentEntity = new BackendPool();
        final String parentId = parentEntity.getClass().getSimpleName().toLowerCase()+"Test";

        parentEntity.setParentId(parentId);
        entity.setParentId(parentId);

        serviceImplemented.onEvent(makeEvent(Action.ADD, parentEntity));
        serviceImplemented.onEvent(makeEvent(Action.ADD, entity));
        serviceImplemented.onEvent(makeEvent(Action.DEL, entity));
        assertThat(farm.getBackends()).extracting("id").doesNotContain(entity.getId());

    }

    @Test
    public void onEventDelBackendPoolTest() {
        final Entity entity = new BackendPool();

        serviceImplemented.onEvent(makeEvent(Action.ADD, entity));
        serviceImplemented.onEvent(makeEvent(Action.DEL, entity));
        assertThat(farm.getBackendPools()).extracting("id").doesNotContain(entity.getId());
    }

    @Test
    public void onEventDelVirtualhostTest() {
        final Entity entity = new VirtualHost();

        serviceImplemented.onEvent(makeEvent(Action.ADD, entity));
        serviceImplemented.onEvent(makeEvent(Action.DEL, entity));
        assertThat(farm.getVirtualHosts()).extracting("id").doesNotContain(entity.getId());
    }

    @Test
    public void onEventDelRuleTest() {
        final Entity entity = new Rule();
        final Entity parentEntity = new VirtualHost();
        final String parentId = parentEntity.getClass().getSimpleName().toLowerCase()+"Test";

        parentEntity.setParentId(parentId);
        entity.setParentId(parentId);

        serviceImplemented.onEvent(makeEvent(Action.ADD, parentEntity));
        serviceImplemented.onEvent(makeEvent(Action.ADD, entity));
        serviceImplemented.onEvent(makeEvent(Action.DEL, entity));
        assertThat(farm.getRules()).extracting("id").doesNotContain(entity.getId());
    }

}
