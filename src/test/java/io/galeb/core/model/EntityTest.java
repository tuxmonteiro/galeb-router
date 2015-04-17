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
        assertThat(entity.getHash()).isEmpty();
    }

    @Test
    public void hashUpdatedInNotEmpty() {
        entity.updateHash();
        assertThat(entity.getHash()).isNotEmpty();
    }

    @Test
    public void newPropertiesOverrideOldProperties() {
        entity.getProperties().clear();

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
