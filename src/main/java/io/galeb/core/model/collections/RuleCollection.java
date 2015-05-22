package io.galeb.core.model.collections;

import io.galeb.core.json.JsonObject;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Rule;
import io.galeb.core.model.VirtualHost;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class RuleCollection extends CopyOnWriteArraySet<Rule> implements Collection<Rule, VirtualHost> {

    private static final long serialVersionUID = -8989216690170245073L;

    private Set<VirtualHost> virtualHosts;

    @Override
    public Collection<Rule, VirtualHost> defineSetOfRelatives(Set<VirtualHost> relatives) {
        this.virtualHosts = relatives;
        return this;
    }

    @Override
    public Collection<Rule, VirtualHost> addToParent(VirtualHost virtualHost, Rule rule) {
        virtualHost.addRule(rule);
        return this;
    }

    @Override
    public List<Rule> getListByID(String entityId) {
        return stream().filter(entity -> entity.getId().equals(entityId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Rule> getListByJson(JsonObject json) {
        final Entity entity = (Entity) json.instanceOf(Entity.class);
        return getListByID(entity.getId());
    }

    @Override
    public boolean add(Rule rule) {
        final boolean result = false;
        if (!contains(rule)) {
            virtualHosts.stream()
                .filter(virtualHost -> virtualHost.getId().equals(rule.getParentId()))
                .forEach(virtualHost -> addToParent(virtualHost, rule));
            super.add(rule);
        }
        return result;
    }

    @Override
    public boolean remove(Object rule) {
        final String ruleId = ((Entity) rule).getId();
        virtualHosts.stream()
            .filter(virtualHost -> virtualHost.containRule(ruleId))
            .forEach(virtualHost -> virtualHost.delRule(ruleId));
        return super.remove(rule);
    }

    @Override
    public Collection<Rule, VirtualHost> change(Rule rule) {
        if (contains(rule)) {
            final String ruleId = rule.getId();
            virtualHosts.stream().filter(virtualHost -> virtualHost.containRule(ruleId))
                .forEach(virtualHost -> {
                    final Rule myrule = virtualHost.getRule(ruleId);
                    myrule.setProperties(rule.getProperties());
                    myrule.updateHash();
                    myrule.updateModifiedAt();
                });
            stream().filter(myrule -> myrule.equals(rule))
                .forEach(myrule -> {
                    myrule.setProperties(rule.getProperties());
                    myrule.updateHash();
                    myrule.updateModifiedAt();
                });
        }
        return this;
    }

    @Override
    public void clear() {
        stream().forEach(rule -> this.remove(rule));
    }

}
