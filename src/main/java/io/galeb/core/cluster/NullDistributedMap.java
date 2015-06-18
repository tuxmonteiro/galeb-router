package io.galeb.core.cluster;

import io.galeb.core.model.Entity;

import javax.enterprise.inject.Alternative;

@Alternative
public class NullDistributedMap implements DistributedMap<String, Entity> {
    // NULL PATTERN
}
