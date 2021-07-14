<h1 align="center">Welcome to JustNetty 👋</h1>
<p>
  <img alt="Version" src="https://img.shields.io/badge/version-1.0.0-blue.svg?cacheSeconds=2592000" />
  <a href="#" target="_blank">
    <img alt="License: ISC" src="https://img.shields.io/badge/License-ISC-yellow.svg" />
  </a>
</p>

> JustNetty, ❤ a netty ssl demo ❤.

### 🏠 [Homepage-gitee](https://gitee.com/ahviplc/JustNetty)

### 🏠 [Homepage-github](https://github.com/ahviplc/JustNetty)

## Install

```markdown

```

## Usage

```sh

```

## Links

```markdown
Netty: Home
https://netty.io/

GitHub - netty/netty: Netty project - an event-driven asynchronous network application framework
https://github.com/netty/netty

极好的
Netty实现SSL单向示例
https://blog.csdn.net/russle/article/details/99086684

springboot/nettyssl at master · yqbjtu/springboot · GitHub
https://github.com/yqbjtu/springboot/tree/master/nettyssl

README.md - yqbjtu/springboot - GitHub1s
https://github1s.com/yqbjtu/springboot/blob/master/nettyssl/README.md

Maven Repository: io.netty » netty-all | all in one 包含了 netty-codec
https://mvnrepository.com/artifact/io.netty/netty-all

Maven Repository: io.netty » netty-codec
https://mvnrepository.com/artifact/io.netty/netty-codec

Maven Repository: org.projectlombok » lombok
https://mvnrepository.com/artifact/org.projectlombok/lombok

Hutool — 🍬A set of tools that keep Java sweet.
https://www.hutool.cn/

Maven Repository: cn.hutool » hutool-all
https://mvnrepository.com/artifact/cn.hutool/hutool-all

Maven Repository: org.slf4j » slf4j-api
https://mvnrepository.com/artifact/org.slf4j/slf4j-api

扩展 不错的
netty使用ssl双向认证 - 知乎
https://zhuanlan.zhihu.com/p/26950051

优雅shutdown
netty集成ssl完整参考指南（含完整源码） - zhjh256 - 博客园
https://www.cnblogs.com/zhjh256/p/6488668.html
```

## Notes

### 1. 随记

```markdown

```

### 2. 注意点

```markdown

```

### 3. 小知识

```markdown
添加jvm参数 
-Djavax.net.debug=ssl,handshake 
来查看ssl握手过程控制台的log
```

### 4. 代码段

```java
class shutdown {
	public static void shutdown() {  
    logger.debug("preparing to shutdown spider server...");
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();  
    logger.debug("spider server is shutdown.");
    }
}
```

```markdown

```

## Author

👤 **LC ahlc@sina.cn**

## Show your support

Give a ⭐️if this project helped you!

***

_This README was generated with ❤️by
[readme-md-generator](https://github.com/kefranabg/readme-md-generator)_
