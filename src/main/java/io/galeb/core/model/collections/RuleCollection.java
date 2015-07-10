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

public class RuleCollection implements Collection<Rule, VirtualHost> {

    private Set<Entity> rules = new CopyOnWriteArraySet<>();

    private Collection<? extends Entity, ? extends Entity> virtualHosts;

    @Override
    public Collection<Rule, VirtualHost> defineSetOfRelatives(Collection<? extends Entity, ? extends Entity> relatives) {
        virtualHosts = relatives;
        return this;
    }

    @Override
    public Collection<Rule, VirtualHost> addToParent(VirtualHost virtualHost, Rule rule) {
        virtualHost.addRule(rule);
        return this;
    }

    @Override
    public List<Entity> getListByID(String entityId) {
        return stream().filter(entity -> entity.getId().equals(entityId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Entity> getListByJson(JsonObject json) {
        final Entity entity = (Entity) json.instanceOf(Entity.class);
        return getListByID(entity.getId());
    }

    @Override
    public boolean add(Entity rule) {
        boolean result = false;
        if (!contains(rule)) {
            virtualHosts.stream()
                .filter(virtualHost -> virtualHost.getId().equals(rule.getParentId()))
                .forEach(virtualHost -> addToParent((VirtualHost)virtualHost, (Rule)rule));
            result = rules.add(rule);
        }
        return result;
    }

    @Override
    public boolean remove(Object rule) {
        boolean result = false;
        if (contains(rule)) {
            final String ruleId = ((Entity) rule).getId();
            virtualHosts.stream()
                .filter(virtualHost -> ((VirtualHost) virtualHost).containRule(ruleId))
                .forEach(virtualHost -> ((VirtualHost) virtualHost).delRule(ruleId));
            result = rules.remove(rule);
        }
        return result;
    }

    @Override
    public Collection<Rule, VirtualHost> change(Entity rule) {
        if (contains(rule)) {
            final String ruleId = rule.getId();
            virtualHosts.stream().filter(virtualHost -> ((VirtualHost) virtualHost).containRule(ruleId))
                .forEach(virtualHost -> {
                    final Rule myrule = ((VirtualHost) virtualHost).getRule(ruleId);
                    myrule.setProperties(rule.getProperties());
                    myrule.updateETag();
                    myrule.updateModifiedAt();
                });
            rules.stream().filter(myrule -> myrule.equals(rule))
                .forEach(myrule -> {
                    myrule.setProperties(rule.getProperties());
                    myrule.updateETag();
                    myrule.updateModifiedAt();
                });
        }
        return this;
    }

    @Override
    public void clear() {
        rules.stream().forEach(rule -> remove(rule));
    }

    @Override
    public int size() {
        return rules.size();
    }

    @Override
    public boolean isEmpty() {
        return rules.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return rules.contains(o);
    }

    @Override
    public Iterator<Entity> iterator() {
        return rules.iterator();
    }

    @Override
    public Object[] toArray() {
        return rules.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return rules.toArray(a);
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        return rules.containsAll(c);
    }

    @Override
    public boolean addAll(java.util.Collection<? extends Entity> c) {
        return rules.addAll(c);
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        return rules.retainAll(c);
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        return rules.removeAll(c);
    }

}
