package com.lc.server;

import com.lc.client.MyClientSslContextFactory;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * Simple to Introduction
 * className: MyClientSslContextFactory
 *
 * @author LC
 * @version v1.0.0
 * @datetime 2021年7月14日10:06:36
 */
@Slf4j
public class MyServerSslContextFactory {
	private static final String PROTOCOL = "TLS";

	private static SSLContext sslContext;

	public static SSLContext getServerContext(String pkPath, String caPath, String storepass, String keypass) {
		if (sslContext != null) return sslContext;
		InputStream in = null;
		InputStream caIn = null;

		try {
			//密钥管理器
			KeyManagerFactory kmf = null;
			if (pkPath != null) {
				//密钥库KeyStore
				KeyStore ks = KeyStore.getInstance("JKS");
				//加载服务端证书
				String resource = pkPath.substring("classpath://".length());
				in = MyServerSslContextFactory.class.getClassLoader().getResourceAsStream(resource);
				//in = new FileInputStream(pkPath);
				//加载服务端的KeyStore,  该密钥库的密码"storepass,storepass指定密钥库的密码(获取keystore信息所需的密码)
				ks.load(in, storepass.toCharArray());

				kmf = KeyManagerFactory.getInstance("SunX509");
				//初始化密钥管理器, keypass 指定别名条目的密码(私钥的密码)
				kmf.init(ks, keypass.toCharArray());
			}

			//信任库 caPath is String，双向认证再开启这一段
			TrustManagerFactory tf = null;

			if (caPath != null) {
				KeyStore tks = KeyStore.getInstance("JKS");
				String resource = caPath.substring("classpath://".length());
				caIn = MyServerSslContextFactory.class.getClassLoader().getResourceAsStream(resource);
				// caIn = new FileInputStream(caPath);
				tks.load(caIn, storepass.toCharArray());
				tf = TrustManagerFactory.getInstance("SunX509");
				tf.init(tks);
			}

			// 获取安全套接字协议（TLS协议）的对象
			sslContext = SSLContext.getInstance(PROTOCOL);
			// 初始化此上下文
			// 参数一：认证的密钥    参数二：对等信任认证，如果双向认证就写成tf.getTrustManagers()
			// 参数三：伪随机数生成器 。 由于单向认证，服务端不用验证客户端，所以第二个参数为null
			// sslContext.init(kmf.getKeyManagers(), null, null);
			// 这里是双向认证 所有 参数二 tf.getTrustManagers()
			sslContext.init(kmf.getKeyManagers(), tf.getTrustManagers(), null);
		} catch (Exception e) {
			throw new Error("Failed to init the server-side SSLContext", e);
		} finally {
			// 关闭 in
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.info("close InputStream in.", e);
				}
			}

			// close caIn 双向证书需要，关闭caIn
			if (caIn != null) {
				try {
					caIn.close();
				} catch (IOException e) {
					log.info("close InputStream caIn.", e);
				}
			}
		}
		return sslContext;
	}
}
