package io.github.skycat.geekstudy.gateway.router;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;

/**
 * LoadBalanceRouter
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-11-04 19:14:29
 */
public class LoadBalanceRouter implements HttpEndpointRouter {
    @Override
    public String route(List<String> endpoints) {
        if (null == endpoints || endpoints.isEmpty()) {
            return StringUtils.EMPTY;
        }
        int index = ThreadLocalRandom.current().nextInt(0, endpoints.size());
        return endpoints.get(index);
    }
}
