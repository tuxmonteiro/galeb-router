package io.galeb.core.model;

import static org.assertj.core.api.Assertions.assertThat;
import io.galeb.core.json.JsonObject;

import org.junit.Before;
import org.junit.Test;

public class FarmTest {

    Farm farm;

    VirtualHost virtualhostNull = null;
    String virtualHostId = "test.localhost";
    String virtualHostId2 = "test2.localhost";
    JsonObject virtualHostIdJson;
    JsonObject virtualHostIdJson2;

    BackendPool backendPoolNull = null;
    String backendPoolId = "backendpool";
    String backendPoolId2 = "backendpool2";
    JsonObject backendPoolIdJson;
    JsonObject backendPoolIdJson2;

    String backendId = "http://0.0.0.0:00";
    String backendId2 = "http://1.1.1.1:11";
    JsonObject backendIdJson;
    JsonObject backendIdJson2;

    String ruleId = "/";
    String ruleId2 = "/test";
    JsonObject ruleIdJson;
    JsonObject ruleIdJson2;

    @Before
    public void setUp() {
        farm = new Farm();
        virtualHostIdJson = JsonObject.toJsonObject(new VirtualHost().setId(virtualHostId));
        virtualHostIdJson2 = JsonObject.toJsonObject(new VirtualHost().setId(virtualHostId2));

        backendPoolIdJson = JsonObject.toJsonObject(new BackendPool().setId(backendPoolId));
        backendPoolIdJson2 = JsonObject.toJsonObject(new BackendPool().setId(backendPoolId2));

        backendIdJson = JsonObject.toJsonObject(new Backend().setId(backendId).setParentId(backendPoolId));
        backendIdJson2 = JsonObject.toJsonObject(new Backend().setId(backendId2));

        ruleIdJson = JsonObject.toJsonObject(new Rule().setId(ruleId).setParentId(virtualHostId));
        ruleIdJson2 = JsonObject.toJsonObject(new Rule().setId(ruleId2));
    }

    @Test
    public void optionsDefaultIsEmptyAtFarm() {
        assertThat(farm.options.isEmpty()).isTrue();
    }

    @Test
    public void setOptionsAtFarm() {
        farm.options.put("key", "value");
        assertThat(farm.options.get("key")).isEqualTo("value");
    }

    @Test
    public void getEntityMapDefaultIsEmptyAtFarm() {
        assertThat(farm.getEntityMap()).isEmpty();
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
        farm.addVirtualHost(virtualHostIdJson2);
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
        farm.addBackendPool(backendPoolIdJson2);
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
    public void getSingleBackendWithoutBackendAtFarm() throws Exception {
        assertThat(farm.getBackend(backendId)).isNull();
    }

    @Test
    public void getSingleBackendAtFarm() throws Exception {
        farm.addBackendPool(backendPoolIdJson);
        farm.addBackend(backendIdJson);
        assertThat(farm.getBackend(backendId)).hasSize(1);
        farm.addBackend(backendIdJson2);
        assertThat(farm.getBackend(backendId)).hasSize(1);
    }

    @Test
    public void delBackendAtFarm() throws Exception {
        farm.addBackendPool(backendPoolIdJson);
        farm.addBackend(backendIdJson);
        assertThat(farm.getBackend(backendId)).hasSize(1);
        farm.delBackend(backendIdJson);
        assertThat(farm.getBackend(backendId)).isNull();
    }

    @Test
    public void delBackendWithNullBackendPoolAtFarm() throws Exception {
        farm.delBackend(backendIdJson2);
        assertThat(farm.getBackend(backendId2)).isNull();
    }

    @Test
    public void getRulesAtFarm() {
        assertThat(farm.getRules()).isEmpty();
    }

    @Test
    public void getSingleRuleWithoutVirtualHostAtFarm() throws Exception {
        assertThat(farm.getRule(virtualHostId)).isNull();
    }

    @Test
    public void getSingleRuleAtFarm() throws Exception {
        farm.addVirtualHost(virtualHostIdJson);
        farm.addRule(ruleIdJson);
        farm.addRule(ruleIdJson2);
        assertThat(farm.getRule(ruleId)).hasSize(1);
    }

    @Test
    public void delRuleAtFarm() throws Exception {
        farm.addVirtualHost(virtualHostIdJson);
        farm.addRule(ruleIdJson);
        assertThat(farm.getRule(ruleId)).hasSize(1);
        farm.delRule(ruleIdJson);
        assertThat(farm.getRule(ruleId)).isNull();
    }

    @Test
    public void delRuleWithNullVirtualHostAtFarm() throws Exception {
        farm.addRule(ruleIdJson2);
        assertThat(farm.getRule(ruleId)).isNull();
    }

}
