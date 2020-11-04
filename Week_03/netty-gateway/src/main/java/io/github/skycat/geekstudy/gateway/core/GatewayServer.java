package io.github.skycat.geekstudy.gateway.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import io.github.skycat.geekstudy.config.GatewayConfig;
import io.github.skycat.geekstudy.util.NamedThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * GatewayServer
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-11-02 16:59:38
 */
@Component
public class GatewayServer implements InitializingBean, ApplicationListener<ApplicationEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayServer.class);
    
    @Autowired
    private GatewayConfig gatewayConfig;
    
    /** boss group */
    private EventLoopGroup bossGroup;
    /** worker group */
    private EventLoopGroup workerGroup;
    /** server channel */
    private Channel serverChannel;
    /** channl初始化Handler */
    private GatewayServerChanelInitializer channelInitializer;
    
    /** 是否启动 */
    private volatile boolean started;
    
    /** init */
    private void init() {
        LOGGER.debug("init gateway server...");
        bossGroup = new NioEventLoopGroup(gatewayConfig.getBossThreadNum(), new NamedThreadFactory("gateway:boss"));
        workerGroup = new NioEventLoopGroup(gatewayConfig.getWorkerThreadNum(), new NamedThreadFactory("gateway:worker"));
        channelInitializer = new GatewayServerChanelInitializer(gatewayConfig);
        LOGGER.debug(gatewayConfig.toString());
    }
    
    /** start */
    private synchronized void start() {
        if (started) {
            LOGGER.error("Gateway服务已启动");
            return;
        }
        
        LOGGER.info("启动Gateway...");
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
                .childOption(ChannelOption.SO_SNDBUF, 32 * 1024);
        
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(channelInitializer);
        try {
            serverChannel = bootstrap.bind(gatewayConfig.getPort()).sync().channel();
            started = true;
            LOGGER.info("Gateway服务启动, 地址[http://127.0.0.1:{}/]", gatewayConfig.getPort());
        } catch (InterruptedException e) {
            LOGGER.error("Gateway服务启动异常", e);
        }
    }
    
    /** shutdown */
    private synchronized void shutdown() {
        if (!started) {
            LOGGER.error("Gateway服务未启动");
            return;
        }
        started = false;
        serverChannel.close().awaitUninterruptibly();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationStartedEvent) {
            // start
            start();
            return;
        }
        if (event instanceof ContextClosedEvent) {
            shutdown();
            return;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
