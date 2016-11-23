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

package io.galeb.services.router.api.jaxrs;

import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.io.*;
import java.lang.management.ManagementFactory;

@Path("/")
public class ApiResources {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String VERSION = "3.1.11";

    enum Method {
        GET,
        POST,
        PUT,
        DELETE,
        PATCH,
        OPTION
    }

    @Context UriInfo uriInfo;

    @Context Request request;

    @Context Application application;

    public ApiResources() {
    }

    private void logReceived(String uri, String body, Method method) {
        LOGGER.info("[" + method.toString() + "] " + uri + ": " + body);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getNull() {
        logReceived("/", "", Method.GET);
        return Response.status(Status.FORBIDDEN).build();
    }

    @GET
    @Path("info")
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    public Response info() {
        long uptimeJVM = ManagementFactory.getRuntimeMXBean().getUptime();
        String uptime = getUptimeCommand();
        String version = getClass().getPackage().getImplementationVersion();
        String infoJson = new JSONObject().accumulate("uptime", uptime).accumulate("uptime-jvm", uptimeJVM).accumulate("version", version).toString();
        return Response.ok(infoJson).build();
    }

    public String getUptimeCommand() {
        ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", "uptime");
        processBuilder.redirectErrorStream(true);
        try {
            Process process = processBuilder.start();
            InputStream stream = process.getInputStream();
            return convertStreamToString(stream).replace("\n", "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String convertStreamToString(InputStream is) throws IOException {

        if (is != null) {
            final Writer writer = new StringWriter();

            final char[] buffer = new char[1024];
            try {
                final Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8")); //$NON-NLS-1$
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        }
        return ""; //$NON-NLS-1$
    }

}
