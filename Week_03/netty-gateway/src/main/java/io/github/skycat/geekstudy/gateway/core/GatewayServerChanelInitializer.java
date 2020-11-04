package io.github.skycat.geekstudy.gateway.core;

import io.github.skycat.geekstudy.config.GatewayConfig;
import io.github.skycat.geekstudy.util.NamedThreadFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * GatewayServerChanelInitializer
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-11-03 10:47:04
 */
public class GatewayServerChanelInitializer extends ChannelInitializer<SocketChannel> {
    /** config */
    private GatewayConfig gatewayConfig;
    /** business executor */
    private EventExecutorGroup executor;
    /** proxy handler */
    private ChannelHandler proxyHandler;
    /** logging */
    //private LoggingHandler loggingHandler;
    
    /**
     * constructor
     * @param gatewayConfig
     */
    public GatewayServerChanelInitializer(GatewayConfig gatewayConfig) {
        this.gatewayConfig = gatewayConfig;
        this.executor = buildExecutor();
        //loggingHandler = new LoggingHandler();
        proxyHandler = new ProxyHandler();
    }
    
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //pipeline.addLast("logging", loggingHandler);
        pipeline.addLast("httpCodec", new HttpServerCodec())
            .addLast(new HttpObjectAggregator(1024 * 1024))
            .addLast(executor, "proxy", proxyHandler);
    }
    
    /** executor */
    private EventExecutorGroup buildExecutor() {
        ThreadGroup threadGroup = new ThreadGroup("gateway");
        NamedThreadFactory threadFactory = new NamedThreadFactory("handler", threadGroup);
        return new DefaultEventExecutorGroup(gatewayConfig.getBusinessThreadNum(), threadFactory);
    }
}
