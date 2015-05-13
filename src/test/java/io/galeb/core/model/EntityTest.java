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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EntityTest {

    private final Entity entity = new Entity();

    @Test
    public void defaultIdIsEmpty() {
        assertThat(entity.getId()).isEmpty();
    }

    @Test
    public void defaultParentIdIsEmpty() {
        assertThat(entity.getParentId()).isEmpty();
    }

    @Test
    public void defaultPkIsNegative() {
        assertThat(entity.getPk()).isNegative();
    }

    @Test
    public void defaultPropertiesIsEmpty() {
        assertThat(entity.getProperties()).isEmpty();
    }

    @Test
    public void defaultEntityTypeIsNotNull() {
        assertThat(entity.getEntityType()).isNotNull();
    }

    @Test
    public void defaultHashIsEmpty() {
        assertThat(entity.getHash()).isEqualTo("0");
    }

    @Test
    public void defaultVersionIsZero() {
        assertThat(entity.getVersion()).isEqualTo(0);
    }

    @Test
    public void hashUpdatedInNotEmpty() {
        entity.updateHash();
        assertThat(entity.getHash()).isNotEmpty();
    }

    @Test
    public void newPropertiesOverrideOldProperties() {
        entity.clearProperties();

        final Map<String, Object> newProperties = new HashMap<>();
        final Map<String, Object> oldProperties = entity.getProperties();
        final String newFirstKey = "new #1";

        oldProperties.put("old #1", true);
        oldProperties.put("old #2", true);
        oldProperties.put("old #3", true);
        newProperties.put(newFirstKey, true);
        entity.setProperties(newProperties);

        assertThat(entity.getProperties()).hasSize(1).containsKey(newFirstKey);
    }
}
