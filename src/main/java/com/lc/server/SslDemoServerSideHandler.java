package com.lc.server;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.log.StaticLog;
import com.lc.utils.SocketUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@NoArgsConstructor
public class SslDemoServerSideHandler extends SimpleChannelInboundHandler<String> {
	private AtomicInteger counter = new AtomicInteger(0);

	/**
	 * 建立连接时触发
	 *
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		log.info("888 ---Connection Created from {}", ctx.channel().remoteAddress());
		Console.log("888 ---Connection Created from {}", ctx.channel().remoteAddress());
		SocketUtils.sendHello(ctx, " 999 server ", false);
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
		Console.log("...SslDemoServerSideHandler channelInactive...{}", ctx.channel().remoteAddress());
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
	public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		// Send the received message to all channels but the current one.
		log.info("aaa ip:{}--- 收到的msg:{}", ctx.channel().remoteAddress(), msg);
		Console.log("aaa ip:【{}】--- 收到的msg:【{}】", ctx.channel().remoteAddress(), msg);

		String reply = "bbb Server counter " + counter.getAndAdd(1) + " ❤11 " + RandomUtil.randomString(6);
		SocketUtils.sendLineBaseText(ctx, reply);
	}

	/**
	 * 出异常的时候 触发
	 *
	 * @param ctx
	 * @param cause
	 * @throws Exception
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.warn("Unexpected exception from downstream.", cause);
		Console.error("Unexpected exception from downstream. {}", cause);
		ctx.close();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state().equals(IdleState.READER_IDLE)) {
				log.info("READER_IDLE");
				StaticLog.info("READER_IDLE Server");
				// 超时关闭channel
				ctx.close();
			} else if (event.state().equals(IdleState.WRITER_IDLE)) {
				log.info("WRITER_IDLE");
				StaticLog.info("WRITER_IDLE Server");
			} else if (event.state().equals(IdleState.ALL_IDLE)) {
				log.info("ALL_IDLE");
				StaticLog.info("ALL_IDLE Server");
				// 发送心跳
				ctx.channel().write("ping\n");
			}
		}
		super.userEventTriggered(ctx, evt);
	}
}
