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
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Entity;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class BackendCollection implements Collection<Backend, BackendPool> {

    private Set<Entity> backends = new CopyOnWriteArraySet<>();

    private Collection<? extends Entity, ? extends Entity> backendPools;

    @Override
    public Collection<Backend, BackendPool> defineSetOfRelatives(Collection<? extends Entity, ? extends Entity> relatives) {
        backendPools = relatives;
        return this;
    }

    @Override
    public Collection<Backend, BackendPool> addToParent(BackendPool backendPool, Backend backend) {
        backendPool.addBackend(backend);
        return this;
    }

    @Override
    public List<Entity> getListByID(String entityId) {
        return backends.stream().filter(entity -> entity.getId().equals(entityId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Entity> getListByJson(JsonObject json) {
        final Entity entity = (Entity) json.instanceOf(Entity.class);
        return getListByID(entity.getId());
    }

    @Override
    public boolean add(Entity backend) {
        final boolean result = false;
        if (!contains(backend)) {
            backendPools.stream()
                .filter(backendPool -> backendPool.getId().equals(backend.getParentId()))
                .forEach(backendPool -> addToParent((BackendPool) backendPool, (Backend) backend));
            backends.add(backend);
        }
        return result;
    }

    @Override
    public boolean remove(Object backend) {
        final String backendId = ((Entity) backend).getId();
        backendPools.stream()
            .filter(backendPool -> ((BackendPool) backendPool).containBackend(backendId))
            .forEach(backendPool -> ((BackendPool) backendPool).delBackend(backendId));
        return backends.remove(backend);
    }

    @Override
    public Collection<Backend, BackendPool> change(Entity backend) {
        if (contains(backend)) {
            backendPools.stream().filter(backendPool -> ((BackendPool) backendPool).containBackend(backend.getId()))
                .forEach(backendPool -> {
                    final Backend myBackend = ((BackendPool) backendPool).getBackend(backend.getId());
                    myBackend.setHealth(((Backend) backend).getHealth());
                    myBackend.setProperties(backend.getProperties());
                    myBackend.setConnections(((Backend) backend).getConnections());
                    myBackend.updateHash();
                    myBackend.updateModifiedAt();
                });
            backends.stream().filter(myBackend -> myBackend.equals(backend))
                .forEach(myBackend -> {
                    ((Backend) myBackend).setHealth(((Backend) backend).getHealth());
                    myBackend.setProperties(backend.getProperties());
                    ((Backend) myBackend).setConnections(((Backend) backend).getConnections());
                    myBackend.updateHash();
                    myBackend.updateModifiedAt();
                });
        }
        return this;
    }

    @Override
    public void clear() {
        backends.stream().forEach(backend -> backends.remove(backend));
    }

    @Override
    public int size() {
        return backends.size();
    }

    @Override
    public boolean isEmpty() {
        return backends.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return backends.contains(o);
    }

    @Override
    public Iterator<Entity> iterator() {
        return backends.iterator();
    }

    @Override
    public Object[] toArray() {
        return backends.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return backends.toArray(a);
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        return backends.containsAll(c);
    }

    @Override
    public boolean addAll(java.util.Collection<? extends Entity> c) {
        return backends.addAll(c);
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        return backends.retainAll(c);
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        return backends.removeAll(c);
    }

}
