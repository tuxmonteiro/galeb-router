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

import io.galeb.core.controller.EntityController;
import io.galeb.core.model.collections.BackendCollection;
import io.galeb.core.model.collections.BackendPoolCollection;
import io.galeb.core.model.collections.Collection;
import io.galeb.core.model.collections.NullEntityCollection;
import io.galeb.core.model.collections.RuleCollection;
import io.galeb.core.model.collections.VirtualHostCollection;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.inject.Alternative;

import com.google.gson.annotations.Expose;

@Alternative
public class Farm extends Entity {

    private static final long serialVersionUID = 1L;

    @Expose private final Collection<VirtualHost, Rule> virtualHosts = new VirtualHostCollection();
    @Expose private final Collection<BackendPool, Backend> backendPools = new BackendPoolCollection();
    private final Collection<Backend, BackendPool> backends = new BackendCollection();
    private final Collection<Rule, VirtualHost> rules = new RuleCollection();

    private final Map<String, EntityController> entityMap = new ConcurrentHashMap<>(16, 0.9f, 1);
    private final Map<String, String> options = new ConcurrentHashMap<>();
    private final Map<Class<? extends Entity>, Collection<? extends Entity, ? extends Entity>> mapOfCollection = new HashMap<>();

    public Farm() {
        setEntityType(Farm.class.getSimpleName().toLowerCase());

        virtualHosts.defineSetOfRelatives(rules);
        backendPools.defineSetOfRelatives(backends);
        backends.defineSetOfRelatives(backendPools);
        rules.defineSetOfRelatives(virtualHosts);

        mapOfCollection.put(VirtualHost.class, virtualHosts);
        mapOfCollection.put(BackendPool.class, backendPools);
        mapOfCollection.put(Backend.class, backends);
        mapOfCollection.put(Rule.class, rules);
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public Farm setOptions(Map<String, String> options) {
        this.options.putAll(options);
        return this;
    }

    public Map<String, EntityController> getEntityMap() {
        return entityMap;
    }

    public Collection<? extends Entity, ? extends Entity> getCollection(Class<? extends Entity> entityClass) {
        if (mapOfCollection.containsKey(entityClass)) {
            return mapOfCollection.get(entityClass);
        } else {
            return new NullEntityCollection();
        }
    }

    public void add(Entity entity) {
        getCollection(entity.getClass()).add(entity);
    }

    public void del(Entity entity) {
        getCollection(entity.getClass()).remove(entity);
    }

    public void change(Entity entity) {
        getCollection(entity.getClass()).change(entity);
    }

    public void clear(Class<? extends Entity> entityClass) {
        getCollection(entityClass).clear();
    }

    public Object getRootHandler() {
        return null;
    }
}
