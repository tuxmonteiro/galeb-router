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
import io.galeb.core.model.collections.RuleCollection;
import io.galeb.core.model.collections.VirtualHostCollection;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.inject.Alternative;

import com.google.gson.annotations.Expose;

@Alternative
public class Farm extends Entity {

    private static final long serialVersionUID = 1L;

    private final Set<Backend> backends = new BackendCollection();

    private final Set<Rule> rules = new RuleCollection();

    @Expose private final Set<VirtualHost> virtualHosts = new VirtualHostCollection();

    @Expose private final Set<BackendPool> backendPools = new BackendPoolCollection();

    private final Map<String, EntityController> entityMap = new ConcurrentHashMap<>(16, 0.9f, 1);

    protected final Map<String, String> options = new ConcurrentHashMap<>();

    public Farm() {
        setEntityType(Farm.class.getSimpleName().toLowerCase());
        getVirtualHostsAsCollection().defineSetOfRelatives(rules);
        getBackendPoolsAsCollection().defineSetOfRelatives(backends);
        getBackendsAsCollection().defineSetOfRelatives(backendPools);
        getRulesAsCollection().defineSetOfRelatives(virtualHosts);
    }

    public Farm setOptions(Map<String, String> options) {
        this.options.putAll(options);
        return this;
    }

    public Map<String, EntityController> getEntityMap() {
        return entityMap;
    }

    public Set<VirtualHost> getVirtualHosts() {
        return virtualHosts;
    }

    @SuppressWarnings("unchecked")
    public Collection<VirtualHost, Rule> getVirtualHostsAsCollection() {
        return (Collection<VirtualHost, Rule>)virtualHosts;
    }

    public Set<BackendPool> getBackendPools() {
        return backendPools;
    }

    @SuppressWarnings("unchecked")
    public Collection<BackendPool, Backend> getBackendPoolsAsCollection() {
        return (Collection<BackendPool, Backend>) backendPools;
    }


    public Set<Backend> getBackends() {
        return backends;
    }

    @SuppressWarnings("unchecked")
    public Collection<Backend, BackendPool> getBackendsAsCollection() {
        return (Collection<Backend, BackendPool>) backends;
    }

    public Set<Rule> getRules() {
        return rules;
    }

    @SuppressWarnings("unchecked")
    public Collection<Rule, VirtualHost> getRulesAsCollection() {
        return (Collection<Rule, VirtualHost>) rules;
    }

    public Object getRootHandler() {
        return null;
    }
}
