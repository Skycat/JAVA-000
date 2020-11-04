package io.github.skycat.geekstudy.httpserver.server;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * HttpRequestHandler
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-11-04 17:22:27
 */
@Sharable
public class HttpRequestHandler extends ChannelInboundHandlerAdapter {
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest) msg;
        try {
            // simple response for all request
            FullHttpResponse response = toResponse(HttpResponseStatus.OK, "Hello, nio!");
            writeResponse(ctx, request, response);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
    
    /** to response */
    private FullHttpResponse toResponse(HttpResponseStatus status, String content) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
        response.content().writeBytes(content.getBytes(CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }
    
    /** write response */
    private void writeResponse(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) {
        if (!HttpUtil.isKeepAlive(request)) {
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            ctx.writeAndFlush(response);
        }
    }
}
