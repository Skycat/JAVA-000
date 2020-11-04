package io.github.skycat.geekstudy.gateway.client;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.concurrent.Future;

/**
 * NettyHttpClient
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-11-04 15:02:51
 */
public class NettyHttpClient {
    private static final HttpClient CLIENT = new HttpClient();
    
    /**
     * do get request
     * @param server
     * @param request
     * @param outCtx
     */
    public static void doGetRequest(String server, FullHttpRequest request, ChannelHandlerContext outCtx) {
    	Channel channel = CLIENT.acquire(server);
    	if (null == channel) {
    		return;
    	}
    	FullHttpRequest backRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/");
    	backRequest.headers().set(request.headers()).set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN)
    		.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
    		.set(HttpHeaderNames.CONTENT_LENGTH, 0);
    	CLIENT.getClientHandler().sendRequest(channel, server, backRequest, outCtx);
    }
    
    /**
     * release
     * @param server
     * @param channel
     */
    static void release(String server, Channel channel) {
    	CLIENT.release(server, channel);
    }
}

final class HttpClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);
	/** max connection */
	private static final int MAX_CONNECTION = 10;
	/** bootstrap */
    private Bootstrap bootstrap;
    private EventLoopGroup eventLoopGroup;
    private HttpClientHandler clientHandler;
    /** channel pool */
    private ChannelPoolMap<InetSocketAddress, FixedChannelPool> channelPool;
    /** server url to inetaddress mapping */
    private ConcurrentMap<String, InetSocketAddress> serverMap = new ConcurrentHashMap<>(4);
    
    HttpClient() {
    	// bootstrap
    	bootstrap = new Bootstrap();
    	eventLoopGroup = new NioEventLoopGroup(16);
    	clientHandler = new HttpClientHandler();
        bootstrap.group(eventLoopGroup)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.SO_SNDBUF, 32 * 1024)
            .option(ChannelOption.SO_RCVBUF, 32 * 1024);
        
        // pool
        channelPool = new AbstractChannelPoolMap<InetSocketAddress, FixedChannelPool>() {
			@Override
			protected FixedChannelPool newPool(InetSocketAddress key) {
				return new FixedChannelPool(bootstrap.remoteAddress(key), new ChannelPoolHandler() {
					@Override
					public void channelReleased(Channel ch) throws Exception {
						LOGGER.debug("released channel[{} - {}]", ch.remoteAddress(), ch.localAddress());
					}
					
					@Override
					public void channelCreated(Channel ch) throws Exception {
						LOGGER.debug("created channel[{} - {}]", ch.remoteAddress(), ch.localAddress());
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new HttpClientCodec())
						.addLast(new HttpObjectAggregator(1024 * 1024))
						.addLast(clientHandler);
					}
					
					@Override
					public void channelAcquired(Channel ch) throws Exception {
						LOGGER.debug("acquired channel[{} - {}]", ch.remoteAddress(), ch.localAddress());
					}
				}, MAX_CONNECTION);
			}
		};
	}
    
    /**
     * acquire
     * @param server
     * @return
     */
    Channel acquire(String server) {
    	InetSocketAddress socketAddress = getInetSocketAddress(server);
    	FixedChannelPool pool = channelPool.get(socketAddress);
    	Future<Channel> future = pool.acquire();
    	try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("acquired channel for [{}] failed", server, e);
			return null;
		}
    }
    
    /**
     * release
     * @param server
     * @param channel
     */
    void release(String server, Channel channel) {
    	InetSocketAddress socketAddress = getInetSocketAddress(server);
    	FixedChannelPool pool = channelPool.get(socketAddress);
    	pool.release(channel);
    }
    
    /** shutdown */
    void shutdown() {
    	eventLoopGroup.shutdownGracefully();
    }
    
    /**
     * HttpClientHandler
     * @return
     */
    public HttpClientHandler getClientHandler() {
		return clientHandler;
	}
    
    /**
     * getInetSocketAddress
     * @param server
     * @return
     */
    InetSocketAddress getInetSocketAddress(String server) {
    	if (!StringUtils.endsWith(server, "/")) {
    		server = server + "/";
    	}
    	InetSocketAddress socketAddress = serverMap.get(server);
    	if (null != socketAddress) {
    		return socketAddress;
    	}
    	String host = StringUtils.substringBetween(server, "http://", ":");
    	int port = Integer.parseInt(StringUtils.substringAfter(StringUtils.substringBetween(server, "http://", "/"), ":"));
    	socketAddress = new InetSocketAddress(host, port);
    	InetSocketAddress prev = serverMap.putIfAbsent(server, socketAddress);
    	if (null != prev) {
    		socketAddress = prev;
    	}
    	return socketAddress;
    }
}