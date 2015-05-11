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

package io.galeb.core.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonObject {

    private final String json;

    private static final Gson jsonParser    = new GsonBuilder().setPrettyPrinting()
                                                               .excludeFieldsWithoutExposeAnnotation()
                                                               .create();

    public static final String NULL = jsonParser.toJson(null);

    public static String toJsonString(Object obj) {
        return jsonParser.toJson(obj);
    }

    public static JsonObject toJsonObject(Object obj) {
        return new JsonObject(toJsonString(obj));
    }

    public static Object fromJson(final String json, final Class<?> aClass) {
        return jsonParser.fromJson(json, aClass);
    }

    public Object instanceOf(final Class<?> aClass) {
        return fromJson(json, aClass);
    }

    public JsonObject(String json) {
        this.json = json;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
               && obj.toString().equals(this.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public String toString() {
        return json;
    }

}
