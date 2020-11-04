package io.github.skycat.geekstudy.gateway.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.skycat.geekstudy.gateway.client.NettyHttpClient;
import io.github.skycat.geekstudy.gateway.filter.HttpRequestFilterChain;
import io.github.skycat.geekstudy.gateway.filter.HttpRequestHeaderFilter;
import io.github.skycat.geekstudy.gateway.router.HttpEndpointRouter;
import io.github.skycat.geekstudy.gateway.router.LoadBalanceRouter;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * ProxyHandler
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-11-03 16:53:44
 */
@Sharable
public class ProxyHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyHandler.class);
    
    /** proxyed servers */
    private Set<String> proxyedServers = ConcurrentHashMap.newKeySet();
    /** copy of proxyed servers */
    private volatile List<String> serverList = new ArrayList<String>();
    /** load balance router */
    private HttpEndpointRouter loadBalanceRouter = new LoadBalanceRouter();
    /** request filter chain */
    private HttpRequestFilterChain filterChain = new HttpRequestFilterChain();
    
    public ProxyHandler() {
        filterChain.addLast(new HttpRequestHeaderFilter());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest) msg;
        //LOGGER.debug("request: {}", request);
        try {
            if (!"/api/registerServer".equals(request.uri())) {
                String route = loadBalanceRouter.route(serverList);
                if (StringUtils.isEmpty(route)) {
                    FullHttpResponse response = toResponse(HttpResponseStatus.GATEWAY_TIMEOUT, "504 Gateway Timeout");
                    writeResponse(ctx, request, response);
                    return;
                }
                //LOGGER.debug("request server: {}", route);
                
                // filter
                filterChain.doFilter(request, ctx);
                //LOGGER.debug("headers: {}", request.headers());
                
                NettyHttpClient.doGetRequest(route, request, ctx);
                
                //FullHttpResponse response = toResponse(HttpResponseStatus.OK, "will be request proxyed server");
                //writeResponse(ctx, request, response);
            } else {
                registerServer(ctx, request);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("链接[{}]请求处理异常", ctx.channel().id().asLongText(), cause);
    }
    
    /** register proxyed server url */
    private void registerServer(ChannelHandlerContext ctx, FullHttpRequest request) {
        if (!HttpMethod.POST.equals(request.method())) {
            // invalid request method
            LOGGER.error("链接[{}]错误的请求方法: {}", ctx.channel().id().asLongText(), request.method());
            FullHttpResponse response = toResponse(HttpResponseStatus.METHOD_NOT_ALLOWED, "405 Method Not Allowed");
            writeResponse(ctx, request, response);
            return;
        }
        if (request.content().isReadable()) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
            List<InterfaceHttpData> httpDatas = decoder.getBodyHttpDatas();
            try {
                for (InterfaceHttpData data : httpDatas) {
                    Attribute attribute = (Attribute) data;
                    LOGGER.debug("{} = {}", attribute.getName(), attribute.getValue());
                    String name = attribute.getName();
                    if ("server".equals(name)) {
                        String url = attribute.getValue();
                        if (StringUtils.isBlank(url)) {
                            break;  // invalid parameter
                        }
                        proxyedServers.add(url);
                        LOGGER.info("注册代理服务器: {}, servers: {}", url, proxyedServers);
                        serverList = new ArrayList<String>(proxyedServers);
                        FullHttpResponse response = toResponse(HttpResponseStatus.OK, "");
                        writeResponse(ctx, request, response);
                        return;
                    }
                }
            } catch (IOException e) {
                LOGGER.error("链接[{}]请求参数异常", ctx.channel().id().asLongText(), e);
            }
            
            // invalid request or invalid parameter
            FullHttpResponse response = toResponse(HttpResponseStatus.BAD_REQUEST, "400 Bad Request");
            writeResponse(ctx, request, response);
            return;
        }
        
        FullHttpResponse response = toResponse(HttpResponseStatus.OK, "");
        writeResponse(ctx, request, response);
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
