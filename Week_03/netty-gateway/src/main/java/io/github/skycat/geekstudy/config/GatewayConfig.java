package io.github.skycat.geekstudy.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * GatewayConfig
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-11-02 17:01:21
 */
@Component
public class GatewayConfig {
    /** port */
    @Value("${proxy.server.port:8888}")
    private int port;
    
    /** boss thread num */
    @Value("${proxy.server.bossThreadNum:1}")
    private int bossThreadNum;
    
    /** worker thread num */
    @Value("${proxy.server.workerThreadNum:8}")
    private int workerThreadNum;
    
    /** business thread num */
    @Value("${proxy.server.businessThreadNum:16}")
    private int businessThreadNum;
    
    /** proxy server port */
    public int getPort() {
        return port;
    }
    
    /** boss thread num */
    public int getBossThreadNum() {
        return bossThreadNum;
    }
    
    /** worker thread num */
    public int getWorkerThreadNum() {
        return workerThreadNum;
    }
    
    /** business thread num */
    public int getBusinessThreadNum() {
        return businessThreadNum;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
