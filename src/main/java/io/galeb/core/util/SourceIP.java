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

package io.galeb.core.util;

import java.util.Optional;

public interface SourceIP {

    public static final String DEFAULT_SOURCE_IP = "127.0.0.1";

    //Useful http headers
    public static final String HTTP_HEADER_XREAL_IP         = "X-Real-IP";
    public static final String HTTP_HEADER_X_FORWARDED_FOR  = "X-Forwarded-For";

    public static final String IGNORE_XFORWARDED_FOR_PROPERTY = "ignore_xforwarded_for";
    public static final Optional<String> IGNORE_XFORWARDED_FOR =
            Optional.ofNullable(System.getProperty(SourceIP.class.getPackage().getName()+
                    IGNORE_XFORWARDED_FOR_PROPERTY));

    public String getRealSourceIP();

    public SourceIP pullFrom(final Object extractable);

}
