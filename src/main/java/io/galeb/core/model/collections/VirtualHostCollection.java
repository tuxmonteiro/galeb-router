package io.galeb.core.model.collections;

import io.galeb.core.json.JsonObject;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Rule;
import io.galeb.core.model.VirtualHost;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class VirtualHostCollection extends CopyOnWriteArraySet<VirtualHost> implements Collection<VirtualHost, Rule> {

    private static final long serialVersionUID = -7921829559046430770L;

    private Set<Rule> rules;

    @Override
    public Collection<VirtualHost, Rule> defineSetOfRelatives(final Set<Rule> relatives) {
        this.rules = relatives;
        return this;
    }

    @Override
    public Collection<VirtualHost, Rule> addChild(Rule child) {
        rules.add(child);
        return this;
    }

    @Override
    public List<VirtualHost> getListByID(String entityId) {
        return stream().filter(entity -> entity.getId().equals(entityId))
                .collect(Collectors.toList());
    }

    @Override
    public List<VirtualHost> getListByJson(JsonObject json) {
        final Entity entity = (Entity) json.instanceOf(Entity.class);
        return getListByID(entity.getId());
    }

    @Override
    public boolean add(VirtualHost virtualhost) {
        boolean result = false;
        if (!contains(virtualhost)) {
            rules.stream().filter(rule -> rule.getParentId().equals(virtualhost.getId()))
                          .forEach(rule -> addChild(rule));
            result = super.add(virtualhost);
        }
        return result;
    }

    @Override
    public Collection<VirtualHost, Rule> change(VirtualHost virtualhost) {
        if (contains(virtualhost)) {
            remove(virtualhost);
            add(virtualhost);
        }
        return this;
    }

    @Override
    public void clear() {
        stream().forEach(virtualhost -> this.remove(virtualhost));
    }

}
