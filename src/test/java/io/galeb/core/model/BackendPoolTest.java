package io.galeb.core.model;

import static org.assertj.core.api.Assertions.assertThat;
import io.galeb.core.json.JsonObject;

import org.junit.Before;
import org.junit.Test;

public class BackendPoolTest {

    BackendPool backendPool;
    Backend nullBackend = null;
    String backendId = "http://0.0.0.0:00";
    String backendId2 = "http://1.1.1.1:11";
    String backendIdJson;
    String backendIdJson2;

    @Before
    public void setUp() {
        backendPool = new BackendPool();
        backendIdJson = JsonObject.toJsonString(new Backend().setId(backendId));
        backendIdJson2 = JsonObject.toJsonString(new Backend().setId(backendId2));
    }

    @Test
    public void getBackendsAtBackendPool() {
        assertThat(backendPool.getBackends()).isEmpty();
    }

    @Test
    public void clearBackendsAtBackendPool() {
        backendPool.addBackend(backendIdJson);
        assertThat(backendPool.getBackends()).hasSize(1);
        backendPool.clearBackends();
        assertThat(backendPool.getBackends()).isEmpty();
    }

    @Test
    public void containBackendIsFalseInDefaultBackendsAtBackendPool() {
        assertThat(backendPool.containBackend(backendId)).isFalse();
    }

    @Test
    public void containBackendIsTrueAfterAddBackendsAtBackendPool() {
        backendPool.addBackend(backendIdJson);
        assertThat(backendPool.containBackend(backendId)).isTrue();
    }

    @Test
    public void delBackendAtBackendPool() {
        backendPool.addBackend(backendIdJson);
        assertThat(backendPool.getBackends()).hasSize(1);
        backendPool.delBackend(backendId);
        assertThat(backendPool.getBackends()).isEmpty();
    }

    @Test
    public void delNullBackendAtBackendPool() {
        backendPool.addBackend(backendIdJson);
        assertThat(backendPool.getBackends()).hasSize(1);
        backendPool.delBackend(nullBackend);
        assertThat(backendPool.getBackends()).hasSize(1);
    }

    @Test
    public void getSingleBackendAtBackendPool() {
        backendPool.addBackend(backendIdJson);
        backendPool.addBackend(backendIdJson2);
        assertThat(backendPool.getBackend(backendId)).isInstanceOf(Backend.class);
    }

}
