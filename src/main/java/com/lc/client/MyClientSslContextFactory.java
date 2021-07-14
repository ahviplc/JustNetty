package com.lc.client;

import cn.hutool.core.lang.Console;
import cn.hutool.log.StaticLog;
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
 * @datetime 2021年7月14日10:12:07
 */
@Slf4j
public class MyClientSslContextFactory {
	private static final String PROTOCOL = "TLS";

	private static SSLContext sslContext;

	public static SSLContext getClientContext(String caPath, String pkPath, String storepass) {
		if (sslContext != null) return sslContext;
		InputStream trustInput = null;
		InputStream keyIn = null;

		try {
			//信任库
			TrustManagerFactory tf = null;
			if (caPath != null) {
				//密钥库KeyStore
				KeyStore trustKeyStore = KeyStore.getInstance("JKS");
				//加载客户端证书
				String resource = caPath.substring("classpath://".length());
				trustInput = MyClientSslContextFactory.class.getClassLoader().getResourceAsStream(resource);
				//trustInput = new FileInputStream(caPath);
				trustKeyStore.load(trustInput, storepass.toCharArray());
				tf = TrustManagerFactory.getInstance("SunX509");
				// 初始化信任库
				tf.init(trustKeyStore);
			}

			// 双向认证时需要加载自己的证书
			KeyManagerFactory kmf = null;
			if (pkPath != null) {
				KeyStore ks = KeyStore.getInstance("JKS");
				String resource = pkPath.substring("classpath://".length());
				keyIn = MyClientSslContextFactory.class.getClassLoader().getResourceAsStream(resource);
				// keyIn = new FileInputStream(pkPath);
				ks.load(keyIn, storepass.toCharArray());
				kmf = KeyManagerFactory.getInstance("SunX509");
				kmf.init(ks, storepass.toCharArray()); // keypass【keypass.toCharArray()】和 storepass 是同一个
			}

			sslContext = SSLContext.getInstance(PROTOCOL);
			//设置信任证书. 双向认证时，第一个参数 kmf.getKeyManagers()
			// sslContext.init(null, tf == null ? null : tf.getTrustManagers(), null);
			// 这里是 双向认证 第一个参数 kmf.getKeyManagers()
			sslContext.init(kmf.getKeyManagers(), tf == null ? null : tf.getTrustManagers(), null);
		} catch (Exception e) {
			Console.error("MyClientSslContextFactory => {}", e);
			throw new Error("Failed to init the client-side SSLContext");
		} finally {
			// 关闭 trustInput
			if (trustInput != null) {
				try {
					trustInput.close();
				} catch (IOException e) {
					log.info("close InputStream trustInput.", e);
					StaticLog.info("close InputStream trustInput.", e);
				}
			}

			// 关闭 keyIn
			if (keyIn != null) {
				try {
					keyIn.close();
				} catch (IOException e) {
					log.info("close InputStream keyIn.", e);
					StaticLog.info("close InputStream keyIn.", e);
				}
			}
		}
		return sslContext;
	}
}
