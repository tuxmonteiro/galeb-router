package io.galeb.core.model;

import static org.assertj.core.api.Assertions.assertThat;
import io.galeb.core.model.Backend.Health;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BackendTest {

    Backend backend;

    @org.junit.Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        backend = new Backend();
    }

    @Test
    public void healthAtBackendHasDefault() {
        assertThat(backend.getHealth()).isEqualTo(Health.HEALTHY);
    }

    @Test
    public void healthCantSetNullAtBackend() {
        exception.expect(NullPointerException.class);
        exception.expectMessage("health");
        backend.setHealth(null);
    }

    @Test
    public void healthHasHealthyAtBackend() {
        assertThat(Health.valueOf("HEALTHY")).isEqualTo(Health.HEALTHY);
    }

    @Test
    public void healthHasDeadyAtBackend() {
        assertThat(Health.valueOf("DEADY")).isEqualTo(Health.DEADY);
    }

    @Test
    public void healthHasUnknownAtBackend() {
        assertThat(Health.valueOf("UNKNOWN")).isEqualTo(Health.UNKNOWN);
    }

    @Test(expected=IllegalArgumentException.class)
    public void healthRaiseExceptionAtBackend() {
        Health.valueOf("TEST");
    }

}
