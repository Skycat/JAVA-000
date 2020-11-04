package io.github.skycat.geekstudy;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;

/**
 * TestNetPool
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2018-09-06 12:21:51
 */
public class TestNetPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestNetPool.class);
    
    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup(4)).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
            }
        });
        ChannelPoolMap<InetSocketAddress, SimpleChannelPool> poolMap = new AbstractChannelPoolMap<InetSocketAddress, SimpleChannelPool>() {
            @Override
            protected SimpleChannelPool newPool(InetSocketAddress key) {
                return new FixedChannelPool(bootstrap.remoteAddress(key),
                                new ChannelPoolHandler() {
                                    @Override
                                    public void channelCreated(Channel ch) throws Exception {
                                        LOGGER.debug("channel created: {}", ch.id().asLongText());
                                    }
                                    
                                    @Override
                                    public void channelReleased(Channel ch) throws Exception {
                                        LOGGER.debug("channel released: {}", ch.id().asLongText());
                                    }
                                    
                                    @Override
                                    public void channelAcquired(Channel ch) throws Exception {
                                        LOGGER.debug("channel acquired: {}", ch.id().asLongText());
                                    }
                                }, 1);
            }
        };
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 8888);
        SimpleChannelPool channelPool = poolMap.get(socketAddress);
        int times = 10;
        for (int i = 1; i <= times; i++) {
            Future<Channel> future = channelPool.acquire();
            try {
                Channel channel = future.get();
                LOGGER.debug("get channel: {}", channel.id().asLongText());
                Thread.sleep(5000);
                channelPool.release(channel);
            } catch (Exception e) {
                LOGGER.error("", e);
            }
        }
        
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
