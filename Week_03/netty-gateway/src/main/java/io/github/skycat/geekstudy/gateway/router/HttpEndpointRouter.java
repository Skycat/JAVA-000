package io.github.skycat.geekstudy.gateway.router;

import java.util.List;

/**
 * HttpEndpointRouter
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-11-04 19:13:17
 */
public interface HttpEndpointRouter {
    /**
     * route
     * @param endpoints
     * @return
     */
    String route(List<String> endpoints);
}
