package io.galeb.core.model;

import static org.assertj.core.api.Assertions.assertThat;
import io.galeb.core.json.JsonObject;

import org.junit.Before;
import org.junit.Test;

public class FarmTest {

    Farm farm;

    VirtualHost virtualhostNull = null;
    String virtualHostId = "test.localhost";
    JsonObject virtualHostIdJson = new JsonObject(String.format("{'id':'%s'}", virtualHostId));
    JsonObject virtualHostId2Json = new JsonObject("{'id':'test2.localhost'}");

    BackendPool backendPoolNull = null;
    String backendPoolId = "backendpool";
    JsonObject backendPoolIdJson = new JsonObject(String.format("{'id':'%s'}", backendPoolId));
    JsonObject backendPoolId2Json = new JsonObject("{'id':'backendpool2'}");

    String backendId = "http://0.0.0.0:00";
    JsonObject backendIdJson = new JsonObject(String.format("{'id':'%s'}", backendId));
    JsonObject backendId2Json = new JsonObject("{'id':'http://1.1.1.1:11'}");

    @Before
    public void setUp() {
        farm = new Farm();
    }

    @Test
    public void getVirtualHostsAtFarm() {
        assertThat(farm.getVirtualHosts()).isEmpty();
    }

    @Test
    public void containVirtualHostIsFalseInDefaultVirtualHostsAtFalse() {
        assertThat(farm.containVirtualHost(virtualHostIdJson)).isFalse();
    }

    @Test
    public void containVirtualHostWithStringIsFalseInDefaultVirtualHostsAtFalse() {
        assertThat(farm.containVirtualHost(virtualHostId)).isFalse();
    }

    @Test
    public void containVirtualHostIsTrueAfterAddVirtualHostAtFarm() throws Exception {
        farm.addVirtualHost(virtualHostIdJson);
        assertThat(farm.containVirtualHost(virtualHostIdJson)).isTrue();
    }

    @Test
    public void containVirtualHostWithStringIsTrueAfterAddVirtualHostAtFarm() throws Exception {
        farm.addVirtualHost(virtualHostIdJson);
        assertThat(farm.containVirtualHost(virtualHostId)).isTrue();
    }

    @Test
    public void clearVirtualHostAtFarm() throws Exception {
        farm.addVirtualHost(virtualHostIdJson);
        assertThat(farm.getVirtualHosts()).hasSize(1);
        farm.clearVirtualHosts();
        assertThat(farm.getVirtualHosts()).isEmpty();
    }

    @Test
    public void getSingleVirtualHostAtFarm() throws Exception {
        farm.addVirtualHost(virtualHostIdJson);
        farm.addVirtualHost(virtualHostId2Json);
        assertThat(farm.getVirtualHost(virtualHostIdJson)).isInstanceOf(VirtualHost.class);
    }

    @Test
    public void delVirtualHostAtFarm() throws Exception {
        farm.addVirtualHost(virtualHostIdJson);
        assertThat(farm.getVirtualHosts()).hasSize(1);
        farm.delVirtualHost(virtualHostIdJson);
        assertThat(farm.getVirtualHosts()).isEmpty();
    }

    @Test
    public void delVirtualHostWithStringAtFarm() throws Exception {
        farm.addVirtualHost(virtualHostIdJson);
        assertThat(farm.getVirtualHosts()).hasSize(1);
        farm.delVirtualHost(virtualHostId);
        assertThat(farm.getVirtualHosts()).isEmpty();
    }

    @Test
    public void delNullVirtualHostAtFarm() throws Exception {
        farm.addVirtualHost(virtualHostIdJson);
        assertThat(farm.getVirtualHosts()).hasSize(1);
        farm.delVirtualHost(virtualhostNull);
        assertThat(farm.getVirtualHosts()).hasSize(1);
    }

    @Test
    public void getBackendPoolsAtFarm() {
        assertThat(farm.getBackendPools()).isEmpty();
    }

    @Test
    public void containBackendPoolIsFalseInDefaultBackendPoolsAtFalse() {
        assertThat(farm.containBackendPool(backendPoolIdJson)).isFalse();
    }

    @Test
    public void containBackendPoolWithStringIsFalseInDefaultBackendPoolsAtFalse() {
        assertThat(farm.containBackendPool(backendPoolId)).isFalse();
    }

    @Test
    public void containBackendPoolIsTrueAfterAddBackendPoolAtFarm() throws Exception {
        farm.addBackendPool(backendPoolIdJson);
        assertThat(farm.containBackendPool(backendPoolIdJson)).isTrue();
    }

    @Test
    public void containBackendPoolWithStringIsTrueAfterAddBackendPoolAtFarm() throws Exception {
        farm.addBackendPool(backendPoolIdJson);
        assertThat(farm.containBackendPool(backendPoolId)).isTrue();
    }

    @Test
    public void clearBackendPoolAtFarm() throws Exception {
        farm.addBackendPool(backendPoolIdJson);
        assertThat(farm.getBackendPools()).hasSize(1);
        farm.clearBackendPool();
        assertThat(farm.getBackendPools()).isEmpty();
    }

    @Test
    public void getSingleBackendPoolAtFarm() throws Exception {
        farm.addBackendPool(backendPoolIdJson);
        farm.addBackendPool(backendPoolId2Json);
        assertThat(farm.getBackendPool(backendPoolIdJson)).isInstanceOf(BackendPool.class);
    }

    @Test
    public void delBackendPoolAtFarm() throws Exception {
        farm.addBackendPool(backendPoolIdJson);
        assertThat(farm.getBackendPools()).hasSize(1);
        farm.delBackendPool(backendPoolIdJson);
        assertThat(farm.getBackendPools()).isEmpty();
    }

    @Test
    public void delBackendPoolWithStringAtFarm() throws Exception {
        farm.addBackendPool(backendPoolIdJson);
        assertThat(farm.getBackendPools()).hasSize(1);
        farm.delBackendPool(backendPoolId);
        assertThat(farm.getBackendPools()).isEmpty();
    }

    @Test
    public void delNullBackendPoolAtFarm() throws Exception {
        farm.addBackendPool(backendPoolIdJson);
        assertThat(farm.getBackendPools()).hasSize(1);
        farm.delBackendPool(backendPoolNull);
        assertThat(farm.getBackendPools()).hasSize(1);
    }

    @Test
    public void getBackendsAtFarm() {
        assertThat(farm.getBackends()).isEmpty();
    }

    @Test
    public void getRulesAtFarm() {
        assertThat(farm.getRules()).isEmpty();
    }

}
