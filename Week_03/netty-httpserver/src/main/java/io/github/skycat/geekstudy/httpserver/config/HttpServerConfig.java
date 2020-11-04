package io.github.skycat.geekstudy.httpserver.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * HttpServerConfig
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-11-04 17:07:11
 */
@Component
public class HttpServerConfig {
    /** default port */
    public static final int DEFAULT_PORT = 8801;
    
    /** boss thread num */
    @Value("${httpserver.bossThreadNum:1}")
    private int bossThreadNum;
    
    /** worker thread num */
    @Value("${httpserver.workerThreadNum:8}")
    private int workerThreadNum;
    
    /** gateway host */
    @Value("${httpserver.gateway.host}")
    private String gatewayHost;
    
    /** gateway port */
    @Value("${httpserver.gateway.port}")
    private int gatewayPort;
    
    /** boss thread num */
    public int getBossThreadNum() {
        return bossThreadNum;
    }
    
    /** worker thread num */
    public int getWorkerThreadNum() {
        return workerThreadNum;
    }
    
    /** gateway host */
    public String getGatewayHost() {
		return gatewayHost;
	}
    
    /** gateway port */
    public int getGatewayPort() {
		return gatewayPort;
	}
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
