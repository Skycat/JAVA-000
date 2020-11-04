package io.github.skycat.geekstudy.httpserver.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * HttpServerChannelInitializer
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-11-04 17:13:05
 */
public class HttpServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    /** http request handler */
    private ChannelHandler requestHandler;
    
    /**
     * constructor
     * @param serverConfig
     */
    public HttpServerChannelInitializer() {
        requestHandler = new HttpRequestHandler();
    }
    
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec())
            .addLast(new HttpObjectAggregator(1024 * 1024))
            .addLast(requestHandler);
    }
}
