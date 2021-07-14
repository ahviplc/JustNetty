package com.lc.server;

import cn.hutool.core.util.RandomUtil;
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

	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		log.info("888 ---Connection Created from {}", ctx.channel().remoteAddress());
		SocketUtils.sendHello(ctx, " 999 server ", false);
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		// Send the received message to all channels but the current one.
		log.info("aaa ip:{}--- msg:{}", ctx.channel().remoteAddress(), msg);

//		String reply = "bbb Server counter " + counter.getAndAdd(1) + " ❤11 " + RandomUtil.randomString(6);
//		SocketUtils.sendLineBaseText(ctx, reply);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.warn("Unexpected exception from downstream.", cause);
		ctx.close();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state().equals(IdleState.READER_IDLE)) {
				log.info("READER_IDLE");
				// 超时关闭channel
				ctx.close();
			} else if (event.state().equals(IdleState.WRITER_IDLE)) {
				log.info("WRITER_IDLE");
			} else if (event.state().equals(IdleState.ALL_IDLE)) {
				log.info("ALL_IDLE");
				// 发送心跳
				ctx.channel().write("ping\n");
			}
		}
		super.userEventTriggered(ctx, evt);
	}
}
