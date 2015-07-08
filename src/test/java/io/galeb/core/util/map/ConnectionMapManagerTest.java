package io.galeb.core.util.map;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConnectionMapManagerTest {

    private final ConnectionMapManager connectionMapManager = ConnectionMapManager.INSTANCE;

    @Before
    public void setUp() {

    }

    @After
    public void cleanUp() {
        connectionMapManager.clear();
    }

    @Test
    public void reduceTest() {
        int z = 0;
        String uri = "UNDEF";
        for (int x=0; x<10; x++) {
            connectionMapManager.putOnCounterMap(uri, Integer.toString(x), x);
            z = z + x;
        }
        assertThat(connectionMapManager.reduce().get(uri)).isEqualTo(z);
    }

}
