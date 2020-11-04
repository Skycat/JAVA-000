package io.github.skycat.geekstudy.gateway.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.ReferenceCountUtil;

/**
 * HttpClientHandler
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-11-05 00:22:33
 */
@Sharable
public class HttpClientHandler extends ChannelInboundHandlerAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientHandler.class);
	/** id -> server */
	private ConcurrentMap<String, RequestContext> waitResponseMap = new ConcurrentHashMap<>();

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		FullHttpResponse response = (FullHttpResponse) msg;
		RequestContext requestContext = waitResponseMap.remove(ctx.channel().id().asLongText());
		if (null == requestContext) {
			LOGGER.error("error response: {}", ctx.channel().id().asLongText());
			ReferenceCountUtil.release(msg);
			return;
		}
		NettyHttpClient.release(requestContext.getServer(), ctx.channel());
		
		// to gateway
		FullHttpResponse outResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, response.status());
		outResponse.content().writeBytes(response.content(), 0, response.content().readableBytes());
		outResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
		outResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, outResponse.content().readableBytes());
		requestContext.getCtx().writeAndFlush(outResponse);
		
		ReferenceCountUtil.release(msg);
	}
	
	/**
	 * sendRequest
	 * @param channel
	 * @param request
	 * @param outCtx
	 */
	public void sendRequest(Channel channel, String server, FullHttpRequest request, ChannelHandlerContext outCtx) {
		waitResponseMap.putIfAbsent(channel.id().asLongText(), new RequestContext(server, outCtx));
		channel.writeAndFlush(request);
	}
}
