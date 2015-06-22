package io.galeb.core.cluster;

import javax.enterprise.inject.Alternative;

@Alternative
public class NullDistributedMapStats implements DistributedMapStats {

    @Override
    public String toString() {
        return getStats();
    }

    @Override
    public String getStats() {
        return this.getClass().getSimpleName();
    }
}
