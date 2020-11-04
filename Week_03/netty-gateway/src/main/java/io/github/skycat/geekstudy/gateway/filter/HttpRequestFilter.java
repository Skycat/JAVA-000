package io.github.skycat.geekstudy.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * HttpRequestFilter
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-11-04 19:29:53
 */
public interface HttpRequestFilter {
    /**
     * filter
     * @param fullRequest
     * @param ctx
     */
    void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx);
}
