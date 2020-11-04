package io.github.skycat.geekstudy.gateway.client;

import io.netty.channel.ChannelHandlerContext;

/**
 * RequestContext
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-11-05 03:10:33
 */
public class RequestContext {
	private String server;
	private ChannelHandlerContext ctx;
	
	public RequestContext(String server, ChannelHandlerContext ctx) {
		this.server = server;
		this.ctx = ctx;
	}

	public String getServer() {
		return server;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}
}
