package io.github.skycat.geekstudy.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;

/**
 * HttpRequestHeaderFilter
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-11-04 19:47:23
 */
public class HttpRequestHeaderFilter implements HttpRequestFilter {
    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        HttpHeaders headers = fullRequest.headers();
        headers.add("nio", "Skycat");
    }
}
