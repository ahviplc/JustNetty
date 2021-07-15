package com.lc.server;

import cn.hutool.core.lang.Console;
import com.lc.utils.SocketUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class MyChannelInboundHandlerAdapterHandle extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		// super.channelRegistered(ctx);
		Console.log("...MyChannelInboundHandlerAdapterHandle channelRegistered...{}", ctx.channel().remoteAddress());
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// super.channelUnregistered(ctx);
		Console.log("...MyChannelInboundHandlerAdapterHandle channelUnregistered...{}", ctx.channel().remoteAddress());
	}

	/**
	 * 建立连接时触发
	 *
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Console.log("...MyChannelInboundHandlerAdapterHandle channelActive...{}", ctx.channel().remoteAddress());
		SocketUtils.sendHello(ctx, "【MyChannelInboundHandlerAdapterHandle channelActive】", false);
		super.channelActive(ctx);
	}

	/**
	 * 连接关闭时触发
	 *
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Console.log("...MyChannelInboundHandlerAdapterHandle channelInactive...{}", ctx.channel().remoteAddress());
		super.channelInactive(ctx);
	}

	/**
	 * 收到消息时触发
	 *
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// super.channelRead(ctx, msg);
		Console.log("...MyChannelInboundHandlerAdapterHandle channelRead...【{}】", ctx.channel().remoteAddress());
		if (msg instanceof ByteBuf) {
			ByteBuf in = (ByteBuf) msg;
			try {
				if (in.readableBytes() > 0) {
					// 转换为String
					String str = in.toString(CharsetUtil.UTF_8);
					Console.log("...MyChannelInboundHandlerAdapterHandle channelRead...receive data:【{}】", str);
				}
			} finally {
				// 释放资源
				ReferenceCountUtil.release(in);
			}
		} else {
			Console.log("...MyChannelInboundHandlerAdapterHandle channelRead...receive data2:【{}】", (String) msg);
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// super.channelReadComplete(ctx);
		Console.log("...MyChannelInboundHandlerAdapterHandle channelReadComplete...{}", ctx.channel().remoteAddress());
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// super.userEventTriggered(ctx, evt);
		Console.log("...MyChannelInboundHandlerAdapterHandle userEventTriggered...{}", ctx.channel().remoteAddress());
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		// super.channelWritabilityChanged(ctx);
		Console.log("...MyChannelInboundHandlerAdapterHandle channelWritabilityChanged...{}", ctx.channel().remoteAddress());
	}

	/**
	 * 出异常的时候 触发
	 *
	 * @param ctx
	 * @param cause
	 * @throws Exception
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// super.exceptionCaught(ctx, cause);
		Console.error("...MyChannelInboundHandlerAdapterHandle exceptionCaught...{}", cause);
		ctx.close();
	}
}
