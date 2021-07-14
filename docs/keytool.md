# 证书生成过程

`版本一 => 合二为一了`

```markdown
参数说明：
        -alias 产生别名
        -validity：指定创建的证书有效期多少天 
        -keyalg ：指定密钥的算法
        -dname ：指定证书拥有者信息  CN=commonName OU=organizationUnit O=organizationName
                                    L=localityName S=stateName C=country
        -keysize 指定密钥长度
        -storepass 指定密钥库的密码
        -keypass 指定别名条目的密码
        -keystore 指定密钥库的名称(产生的各类信息将不在.keystore文件中)
        -export 将别名指定的证书导出到文件 keytool -export -alias caroot -file caroot.crt
        -file 参数指定导出到文件的文件名
```

 ### 1 生成Netty服务端私钥和证书仓库命令
 keytool -genkey -alias mySrvAlias1 -keysize 2048 -validity 365 -keyalg RSA -dname "CN=CN_NETTY_SSL, OU=Developer, O=NETTY_SSL, L=Shanghai, S=Shanghai, C=CH" -keypass skeypass123 -storepass sstorepass456 -keystore netty-ssl-Server.jks

  `同一密码版本`

 keytool -genkey -alias mySrvAlias1 -keysize 2048 -validity 365 -keyalg RSA -dname "CN=CN_NETTY_SSL, OU=Developer, O=NETTY_SSL, L=Shanghai, S=Shanghai, C=CH" -keypass pass123456 -storepass pass123456 -keystore netty-ssl-Server.jks

 -storepass 指定密钥库的密码(获取keystore信息所需的密码)
 -keypass 指定别名条目的密码(私钥的密码)

 ### 2  生成客户端的密钥对和证书仓库，用于将服务端的证书保存到客户端的授信证书仓库中
 keytool -genkey -alias myClientAlias1 -keysize 2048 -validity 365 -keyalg RSA -dname "CN=CN_NETTY_SSL, OU=Developer, O=NETTY_SSL, L=Shanghai, S=Shanghai, C=CH" -keypass ckeypass987 -storepass cstorepass654 -keystore netty-ssl-Client.jks

  `同一密码版本`

 keytool -genkey -alias myClientAlias1 -keysize 2048 -validity 365 -keyalg RSA -dname "CN=CN_NETTY_SSL, OU=Developer, O=NETTY_SSL, L=Shanghai, S=Shanghai, C=CH" -keypass pass123456 -storepass pass123456 -keystore netty-ssl-Client.jks

 ### 3  生成Netty服务端自签名证书 netty-ssl-Server.cer

 keytool -export -alias mySrvAlias1 -keystore netty-ssl-Server.jks -storepass sstorepass456 -file netty-ssl-Server.cer

  `同一密码版本`

 keytool -export -alias mySrvAlias1 -keystore netty-ssl-Server.jks -storepass pass123456 -file netty-ssl-Server.cer

 ### 4  将Netty服务端的证书导入到客户端的证书仓库中：
 (从netty服务端机器那拿到server.cer文件 )
 keytool -import -trustcacerts -alias mySrvAlias1 -file netty-ssl-Server.cer -storepass cstorepass654 -keystore netty-ssl-Client.jks

  `同一密码版本`

 keytool -import -trustcacerts -alias mySrvAlias1 -file netty-ssl-Server.cer -storepass pass123456 -keystore netty-ssl-Client.jks

 如果你只做单向认证，则到此就可以结束了，如果是双响认证，则还需继续往下走

 ### 5  生成客户端自签名证书
   keytool -export -alias myClientAlias1 -keystore netty-ssl-Client.jks -storepass cstorepass654 -file netty-ssl-Client.cer

  `同一密码版本`

   keytool -export -alias myClientAlias1 -keystore netty-ssl-Client.jks -storepass pass123456 -file netty-ssl-Client.cer


 ### 6 将客户端的自签名证书导入到服务端的信任证书仓库中：

   keytool -import -trustcacerts -alias myClientSelfAlias -file netty-ssl-Client.cer -storepass sstorepass456 -keystore netty-ssl-Server.jks

   `同一密码版本`

   keytool -import -trustcacerts -alias myClientSelfAlias -file netty-ssl-Client.cer -storepass pass123456 -keystore netty-ssl-Server.jks

   以上所有步骤都完成后，还可以通过命令来查看 keystore 文件基本信息，如下 所示

   keytool -list -keystore netty-ssl-Client.jks (输入密码 pass123456)
   ---数据结果为----
   C:\_developSoftKu\ideaIU-2019.1.3.win\#CodeKu\JustNetty\src\main\resources\certs>keytool -list -keystore netty-ssl-Client.jks
   输入密钥库口令:
   密钥库类型: jks
   密钥库提供方: SUN

   您的密钥库包含 2 个条目

   myclientalias1, 2019-8-10, PrivateKeyEntry,
   证书指纹 (SHA1): D6:8A:D7:BB:4E:12:60:95:1D:17:51:78:D3:74:D1:00:95:7E:92:08
   mysrvalias1, 2019-8-10, trustedCertEntry,
   证书指纹 (SHA1): A7:F6:DF:C8:83:82:4C:63:8D:24:57:5E:7D:25:58:E0:DE:76:DE:DF

   Warning:
   JKS 密钥库使用专用格式。建议使用 "keytool -importkeystore -srckeystore netty-ssl-Client.jks -destkeystore netty-ssl-Client.jks -deststoretype pkcs12" 迁移到行业标准格式 PKCS12。

   C:\_developSoftKu\ideaIU-2019.1.3.win\#CodeKu\JustNetty\src\main\resources\certs>

    `查看 netty-ssl-Server.jks`
    
    keytool -list -keystore netty-ssl-Server.jks (输入密码 pass123456)

# 其他

  `输出Warning提示`

 ```markdown
    Warning:
    JKS 密钥库使用专用格式。建议使用 "keytool -importkeystore -srckeystore netty-ssl-Client.jks -destkeystore netty-ssl-Client.jks -deststoretype pkcs12" 迁移到行业标准格式 PKCS12。
 
    Warning:
    JKS 密钥库使用专用格式。建议使用 "keytool -importkeystore -srckeystore netty-ssl-Server.jks -destkeystore netty-ssl-Server.jks -deststoretype pkcs12" 迁移到行业标准格式 PKCS12。
 ```

 # 扩展 版本2

 > 在 src/main/resources/certs2 在此生成 在这里 命令行操作哦

 netty使用ssl双向认证 - 知乎
https://zhuanlan.zhihu.com/p/26950051

## 客户端

1. 创建Client端KeyStore文件clientKeys.jks，用于虚构的通信者 Client的证书 ：
>> keytool -genkey -alias myClientAlias -keysize 2048 -validity 365 -keyalg RSA -dname "CN=CN_NETTY_SSL, OU=Developer, O=NETTY_SSL, L=Shanghai, S=Shanghai, C=CH" -keypass pass123456 -storepass pass123456 -keystore netty-ssl-ClientKeys.jks
==>这一步生成了netty-ssl-ClientKeys.jks

2. 导出客户端 CN_NETTY_SSL 公钥证书,并将client.cer文件发给netty服务使用者
>> keytool -export -alias myClientAlias -keystore netty-ssl-ClientKeys.jks -file netty-ssl-Client.cer -storepass pass123456
==>这一步生成netty-ssl-Client.cer

3. 创建客户端KeyStore文件netty-ssl-ClientTrust.jks并导入服务端公钥证书(从netty服务端机器那拿到netty-ssl-Server.cer文件 )
>> keytool -import -alias myClientAlias -keystore netty-ssl-ClientTrust.jks -file netty-ssl-Server.cer -keypass pass123456 -storepass pass123456
==>这一步生成了netty-ssl-ClientTrust.jks

## 服务端

1. 创建server端KeyStore文件serverKeys.jks，用于虚构的通信者 Client的证书 ：
>> keytool -genkey -alias mySrvAlias -keysize 2048 -validity 365 -keyalg RSA -dname "CN=CN_NETTY_SSL, OU=Developer, O=NETTY_SSL, L=Shanghai, S=Shanghai, C=CH" -keypass pass123456 -storepass pass123456 -keystore netty-ssl-ServerKeys.jks
==>这一步生成了netty-ssl-ServerKeys.jks

2. 生成服务端 Alice公钥证书,并将client.cer文件发给netty客户端使用者
>> keytool -export -alias mySrvAlias -keystore netty-ssl-ServerKeys.jks -file netty-ssl-Server.cer -storepass pass123456
==>这一步生成netty-ssl-Server.cer

3. 创建客户端KeyStore文件serverTurst.jks并导入服务端公钥证书(从nettyke客户端机器那拿到netty-ssl-Client.cer文件 )
>> keytool -import -alias mySrvAlias -keystore netty-ssl-ServerTrust.jks -file netty-ssl-Client.cer -keypass pass123456 -storepass pass123456
==>这一步生成了netty-ssl-ServerTrust.jks

## 查看一下

`密码都是 pass123456 ` `src/main/resources/certs2`

```shell
keytool -list -keystore netty-ssl-ClientKeys.jks
keytool -list -keystore netty-ssl-ClientTrust.jks
和
keytool -list -keystore netty-ssl-ServerKeys.jks
keytool -list -keystore netty-ssl-ServerTrust.jks
```
