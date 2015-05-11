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

package io.galeb.core.eventbus;

import io.galeb.core.json.JsonObject;
import io.galeb.core.model.Entity;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Expose
    private final Object type;

    @Expose
    private final Entity data;

    public Event(Object type, Entity data) {
        this.type = type;
        this.data = data;
    }

    public Object getType() {
        return type;
    }

    public JsonObject getData() {
        return JsonObject.toJsonObject(data);
    }

}
