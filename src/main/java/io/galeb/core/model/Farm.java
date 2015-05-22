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
import io.galeb.core.json.JsonObject;
import io.galeb.core.model.collections.BackendCollection;
import io.galeb.core.model.collections.BackendPoolCollection;
import io.galeb.core.model.collections.Collection;
import io.galeb.core.model.collections.RuleCollection;
import io.galeb.core.model.collections.VirtualHostCollection;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

    /**
     *
     * VIRTUALHOST
     *
     */

    public VirtualHost getVirtualHost(String virtualHostId) {
        final List<VirtualHost> listOfVirtualHosts = getVirtualHostsAsCollection().getListByID(virtualHostId);
        if (!listOfVirtualHosts.isEmpty() ) {
            return listOfVirtualHosts.get(0);
        }
        return null;
    }

    public VirtualHost getVirtualHost(JsonObject jsonObject) {
        final List<VirtualHost> listOfVirtualHosts = getVirtualHostsAsCollection().getListByJson(jsonObject);
        if (!listOfVirtualHosts.isEmpty() ) {
            return listOfVirtualHosts.get(0);
        }
        return null;
    }

    public Farm addVirtualHost(JsonObject jsonObject) {
        virtualHosts.add((VirtualHost) jsonObject.instanceOf(VirtualHost.class));
        return this;
    }

    public Farm addVirtualHost(VirtualHost virtualhost) {
        virtualHosts.add(virtualhost);
        return this;
    }

    public Farm changeVirtualHost(JsonObject jsonObject) {
        getVirtualHostsAsCollection()
            .change((VirtualHost) jsonObject.instanceOf(VirtualHost.class));
        return this;
    }

    public Farm changeVirtualHost(VirtualHost virtualHost) {
        getVirtualHostsAsCollection().change(virtualHost);
        return this;
    }

    public Farm delVirtualHost(String virtualhostId) {
        virtualHosts.removeIf(virtualhost -> virtualhost.getId().equals(virtualhostId));
        return this;
    }

    public Farm delVirtualHost(JsonObject jsonObject) {
        virtualHosts.remove(jsonObject.instanceOf(VirtualHost.class));
        return this;
    }

    public Farm delVirtualHost(VirtualHost virtualHost) {
        virtualHosts.remove(virtualHost);
        return this;
    }

    public boolean containVirtualHost(JsonObject jsonObject) {
        return virtualHosts.contains(jsonObject.instanceOf(VirtualHost.class));
    }

    public boolean containVirtualHost(String virtualhostId) {
        return virtualHosts.stream()
                .filter(virtualhost -> virtualhost.getId().equals(virtualhostId))
                .count() > 0L;
    }

    public void clearVirtualHosts() {
        virtualHosts.clear();
    }

    public Set<VirtualHost> getVirtualHosts() {
        return virtualHosts;
    }

    @SuppressWarnings("unchecked")
    public Collection<VirtualHost, Rule> getVirtualHostsAsCollection() {
        return (Collection<VirtualHost, Rule>)virtualHosts;
    }


    /**
     *
     * BACKENDPOOL
     *
     */

    public BackendPool getBackendPool(String backendPoolId) {
        final List<BackendPool> listOfBackendPools = getBackendPoolsAsCollection().getListByID(backendPoolId);
        if (!listOfBackendPools.isEmpty()) {
            return listOfBackendPools.get(0);
        }
        return null;
    }

    public BackendPool getBackendPool(JsonObject jsonObject) {
        final List<BackendPool> listOfBackendPools = getBackendPoolsAsCollection().getListByJson(jsonObject);
        if (!listOfBackendPools.isEmpty()) {
            return listOfBackendPools.get(0);
        }
        return null;
    }

    public Farm addBackendPool(JsonObject jsonObject) {
        backendPools.add((BackendPool) jsonObject.instanceOf(BackendPool.class));
        return this;
    }

    public Farm addBackendPool(BackendPool backendPool) {
        backendPools.add(backendPool);
        return this;
    }

    public Farm changeBackendPool(JsonObject jsonObject) {
        getBackendPoolsAsCollection().change((BackendPool) jsonObject.instanceOf(BackendPool.class));
        return this;
    }

    public Farm changeBackendPool(BackendPool backendPool) {
        getBackendPoolsAsCollection().change(backendPool);
        return this;
    }

    public Farm delBackendPool(JsonObject jsonObject) {
        backendPools.remove(jsonObject.instanceOf(BackendPool.class));
        return this;
    }

    public Farm delBackendPool(String backendId) {
        backendPools.removeIf(backendPool -> backendPool.getId().equals(backendId));
        return this;
    }

    public Farm delBackendPool(BackendPool backendPool) {
        backendPools.remove(backendPool);
        return this;
    }

    public boolean containBackendPool(JsonObject jsonObject) {
        return backendPools.contains(jsonObject.instanceOf(BackendPool.class));
    }

    public boolean containBackendPool(String backendPoolId) {
        return backendPools.stream()
                .filter(backendPool -> backendPool.getId().equals(backendPoolId)).count() > 0L;
    }

    public void clearBackendPool() {
        backendPools.clear();
    }

    public Set<BackendPool> getBackendPools() {
        return backendPools;
    }

    @SuppressWarnings("unchecked")
    public Collection<BackendPool, Backend> getBackendPoolsAsCollection() {
        return (Collection<BackendPool, Backend>) backendPools;
    }

    /**
     *
     * BACKEND
     *
     */

    public Farm addBackend(JsonObject jsonObject) {
        backends.add((Backend) jsonObject.instanceOf(Backend.class));
        return this;
    }

    public Farm addBackend(Backend backend) {
        backends.add(backend);
        return this;
    }

    public Farm changeBackend(JsonObject jsonObject) {
        getBackendsAsCollection().change((Backend) jsonObject.instanceOf(Backend.class));
        return this;
    }

    public Farm changeBackend(Backend backend) {
        getBackendsAsCollection().change(backend);
        return this;
    }

    public Farm delBackend(JsonObject jsonObject) {
        backends.remove(jsonObject.instanceOf(Backend.class));
        return this;
    }

    public Farm delBackend(Backend backend) {
        backends.remove(backend);
        return this;
    }

    public List<Backend> getBackends(String backendId) {
        return backends.stream()
                .filter(backend -> backend.getId().equals(backendId))
                .collect(Collectors.toList());
    }

    public List<Backend> getBackends() {
        return backends.stream().collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public Collection<Backend, BackendPool> getBackendsAsCollection() {
        return (Collection<Backend, BackendPool>) backends;
    }


    /**
     *
     * RULES
     *
     */

    public Farm addRule(JsonObject jsonObject) {
        rules.add((Rule) jsonObject.instanceOf(Rule.class));
        return this;
    }

    public Farm addRule(Rule rule) {
        rules.add(rule);
        return this;
    }

    public Farm delRule(Rule rule) {
        rules.remove(rule);
        return this;
    }

    public Farm delRule(JsonObject jsonObject) {
        rules.remove(jsonObject.instanceOf(Rule.class));
        return this;
    }

    public List<Rule> getRules(String ruleId) {
        return rules.stream().filter(rule -> rule.getId().equals(ruleId)).collect(Collectors.toList());
    }

    public List<Rule> getRules() {
        return rules.stream().collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public Collection<Rule, VirtualHost> getRulesAsCollection() {
        return (Collection<Rule, VirtualHost>) rules;
    }

    public Object getRootHandler() {
        return null;
    }
}
