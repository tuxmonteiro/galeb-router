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

package io.galeb.core.loadbalance.impl;

import io.galeb.core.loadbalance.LoadBalancePolicy;
import io.galeb.core.model.Backend;
import io.galeb.core.model.BackendPool;
import io.galeb.core.model.Entity;
import io.galeb.core.model.Farm;

import java.util.List;
import java.util.Map;

public class StrictLeastConnPolicy extends LoadBalancePolicy {

    private BackendPool backendPool = null;

    @Override
    public int getChoice() {
        if (backendPool!=null) {
            final Backend backendWithLeastConn = backendPool.getBackendWithLeastConn();
            if (backendWithLeastConn!=null) {
                return uris.indexOf(backendWithLeastConn.getId());
            }
        }
        return 0;
    }

    @Override
    public LoadBalancePolicy setCriteria(Map<String, Object> criteria) {
        super.setCriteria(criteria);
        final Object farmObj = loadBalancePolicyCriteria.get(Farm.class.getSimpleName());
        if (farmObj!=null && farmObj instanceof Farm) {
            final Farm farm = (Farm)farmObj;
            final String backendPoolId =
                    (String) loadBalancePolicyCriteria.get(BackendPool.class.getSimpleName());
            final List<Entity> backendPools =
                    farm.getCollection(BackendPool.class).getListByID(backendPoolId);
            if (!backendPools.isEmpty()) {
                backendPool = (BackendPool) backendPools.get(0);
            }
        }
        return this;
    }

}
