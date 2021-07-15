package com.lc.client;

import cn.hutool.log.StaticLog;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class NettyClient {

	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	EventLoopGroup group = new NioEventLoopGroup();

	public void connect(String host, int port) {

		// 配置客户端NIO线程组
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) {

							log.info("client current dir:{}", System.getProperty("user.dir"));
							//String clientPath = (System.getProperty("user.dir")+ "/nettyssl/src/main/resources/certs/netty-ssl-Client.jks");
							String clientPath = "classpath://certs/netty-ssl-Client.jks";
							String pkPath = "classpath://certs/netty-ssl-Client.jks";
							//客户方模式
							SSLContext sslContext =
									MyClientSslContextFactory.getClientContext(clientPath, pkPath, "pass123456");
							SSLEngine sslEngine = sslContext.createSSLEngine();
							sslEngine.setUseClientMode(true);
							ch.pipeline().addLast("ssl", new SslHandler(sslEngine));
							ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
							ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
							ch.pipeline().addLast(new StringDecoder());
							ch.pipeline().addLast("processMsg", new SslDemoClientSideHandler());
						}
					});

			// 发起异步连接操作
			ChannelFuture future = b.connect(host, port).sync();
			future.channel().closeFuture().sync();
		} catch (Exception ex) {
			log.info("connection exception", ex);
			StaticLog.info("connection exception", ex);
		}
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String destIp = "localhost";
		int port = 10002;
		new NettyClient().connect(destIp, port);
	}
}

