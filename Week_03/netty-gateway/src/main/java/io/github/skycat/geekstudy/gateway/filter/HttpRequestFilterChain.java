package io.github.skycat.geekstudy.gateway.filter;

import java.util.LinkedList;
import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * HttpRequestFilterChain
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-11-04 19:38:41
 */
public class HttpRequestFilterChain {
    private List<HttpRequestFilter> filters = new LinkedList<HttpRequestFilter>();
    
    /**
     * addlast
     * @param filter
     * @return
     */
    public HttpRequestFilterChain addLast(HttpRequestFilter filter) {
        filters.add(filter);
        return this;
    }
    
    /**
     * do filter
     * @param fullRequest
     * @param ctx
     */
    public void doFilter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        for (HttpRequestFilter filter : filters) {
            filter.filter(fullRequest, ctx);
        }
    }
}
