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
    public void healthSetDeadyAtBackend() {
        backend.setHealth(Health.DEADY);
        assertThat(backend.getHealth()).isEqualTo(Health.DEADY);
    }

    @Test
    public void healthSetUnknownAtBackend() {
        backend.setHealth(Health.UNKNOWN);
        assertThat(backend.getHealth()).isEqualTo(Health.UNKNOWN);
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
