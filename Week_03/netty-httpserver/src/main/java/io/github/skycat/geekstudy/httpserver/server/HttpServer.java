package io.github.skycat.geekstudy.httpserver.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import io.github.skycat.geekstudy.httpserver.config.HttpServerConfig;
import io.github.skycat.geekstudy.util.NamedThreadFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.internal.SystemPropertyUtil;

/**
 * HttpServer
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-11-04 17:09:32
 */
@Component
public class HttpServer implements InitializingBean, ApplicationListener<ApplicationEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);
    
    @Autowired
    private HttpServerConfig serverConfig;
    
    /** boss group */
    private EventLoopGroup bossGroup;
    /** worker group */
    private EventLoopGroup workerGroup;
    /** server channel */
    private Channel serverChannel;
    /** channl初始化Handler */
    private HttpServerChannelInitializer channelInitializer;
    
    /** 是否启动 */
    private volatile boolean started;
    
    /** init */
    private void init() {
        LOGGER.debug("init http server...");
        bossGroup = new NioEventLoopGroup(serverConfig.getBossThreadNum(), new NamedThreadFactory("httpserver:boss"));
        workerGroup = new NioEventLoopGroup(serverConfig.getWorkerThreadNum(), new NamedThreadFactory("httpserver:worker"));
        channelInitializer = new HttpServerChannelInitializer();
        LOGGER.debug(serverConfig.toString());
    }
    
    /** start */
    private synchronized void start() {
        if (started) {
            LOGGER.error("HttpServer服务已启动");
            return;
        }
        
        LOGGER.info("启动HttpServer...");
        int port = SystemPropertyUtil.getInt("httpserver.port", HttpServerConfig.DEFAULT_PORT);
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
            serverChannel = bootstrap.bind(port).sync().channel();
            started = true;
            LOGGER.info("HttpServer服务启动, 地址[http://127.0.0.1:{}/]", port);
        } catch (InterruptedException e) {
            LOGGER.error("HttpServer服务启动异常", e);
        }
        
        // register to gate way
        Bootstrap clientBootstrap = new Bootstrap();
        clientBootstrap.group(new NioEventLoopGroup(1))
            .channel(NioSocketChannel.class)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.SO_SNDBUF, 32 * 1024)
            .option(ChannelOption.SO_RCVBUF, 32 * 1024)
            .handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new HttpClientCodec())
                        .addLast(new HttpObjectAggregator(1024 * 1024))
                        .addLast(new ChannelInboundHandlerAdapter() {
							@Override
							public void channelActive(ChannelHandlerContext ctx) throws Exception {
								FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/api/registerServer");
								String content = "------WebKitFormBoundaryrGKCBY7qhFd3TrwA\nContent-Disposition: form-data; name=\"server\"\n\n"
										+ "http://127.0.0.1:" + port + "/\n------WebKitFormBoundaryrGKCBY7qhFd3TrwA--";
								request.content().writeBytes(content.getBytes());
						    	request.headers().set(HttpHeaderNames.CONTENT_TYPE, "multipart/form-data; boundary=----WebKitFormBoundaryrGKCBY7qhFd3TrwA")
						    		.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
						    		.set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
						    	ctx.writeAndFlush(request).sync().addListener(ChannelFutureListener.CLOSE);
							}
                        });
                }
            });
        try {
			clientBootstrap.connect(serverConfig.getGatewayHost(), serverConfig.getGatewayPort()).sync();
		} catch (InterruptedException e) {
			LOGGER.error("register to gateway[http://{}:{}/]", serverConfig.getGatewayHost(), serverConfig.getGatewayPort(), e);
		}
    }
    
    /** shutdown */
    private synchronized void shutdown() {
        if (!started) {
            LOGGER.error("HttpServer服务未启动");
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
