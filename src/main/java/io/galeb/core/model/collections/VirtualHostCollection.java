package io.galeb.core.model.collections;

import io.galeb.core.json.JsonObject;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Rule;
import io.galeb.core.model.VirtualHost;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class VirtualHostCollection implements Collection<VirtualHost, Rule> {

    private Set<Entity> virtualhosts = new CopyOnWriteArraySet<Entity>();

    private Collection<? extends Entity, ? extends Entity> rules;

    @Override
    public Collection<VirtualHost, Rule> defineSetOfRelatives(final Collection<? extends Entity, ? extends Entity> relatives) {
        rules = relatives;
        return this;
    }

    @Override
    public Collection<VirtualHost, Rule> addChild(Rule child) {
        rules.add(child);
        return this;
    }

    @Override
    public List<Entity> getListByID(String entityId) {
        return virtualhosts.stream().filter(entity -> entity.getId().equals(entityId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Entity> getListByJson(JsonObject json) {
        final Entity entity = (Entity) json.instanceOf(Entity.class);
        return getListByID(entity.getId());
    }

    @Override
    public boolean add(Entity virtualhost) {
        boolean result = false;
        if (!contains(virtualhost)) {
            rules.stream().filter(rule -> rule.getParentId().equals(virtualhost.getId()))
                          .forEach(rule -> addChild((Rule) rule));
            result = virtualhosts.add(virtualhost);
        }
        return result;
    }

    @Override
    public boolean remove(Object o) {
        return virtualhosts.remove(o);
    }

    @Override
    public Collection<VirtualHost, Rule> change(Entity virtualhost) {
        if (contains(virtualhost)) {
            remove(virtualhost);
            add(virtualhost);
        }
        return this;
    }

    @Override
    public void clear() {
        virtualhosts.stream().forEach(virtualhost -> remove(virtualhost));
    }

    @Override
    public int size() {
        return virtualhosts.size();
    }

    @Override
    public boolean isEmpty() {
        return virtualhosts.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return virtualhosts.contains(o);
    }

    @Override
    public Iterator<Entity> iterator() {
        return virtualhosts.iterator();
    }

    @Override
    public Object[] toArray() {
        return virtualhosts.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return virtualhosts.toArray(a);
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        return virtualhosts.containsAll(c);
    }

    @Override
    public boolean addAll(java.util.Collection<? extends Entity> c) {
        return virtualhosts.addAll(c);
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        return virtualhosts.retainAll(c);
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        return virtualhosts.removeAll(c);
    }

}
