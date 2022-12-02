# Kubejs Koro Extra Tools
## 简介
这个mod为KubeJS加入了许多好玩(X)的功能支持，具体如下：

1. HttpServer
2. HttpClient
3. 文件写入
4. 文件读取
5. 正则表达式支持
6. 异步支持
7. Html处理支持  
8. MQTT客户端
9. WebSocket客户端

所以理论上汝能用这个mod写一个爬虫！  
或着对着小爱来一句：“小爱同学，备份服务器。”（逃  


**此文档部分源自Hutool文档**
## 教程
### HttpServer
因为一些原因，HttpServer是单例化的（主要是担心one boom，all boom）
```javascript
HttpServer.createServer(8620);//在8620端口创建HttpServer对象

HttpServer.register("/test");//注册请求处理规则

//请求事件，这个是使用KubeJS6语法的，不支持KubeJS6以下的（
HttpServerEvents.path("/test",event=>{
    HttpServer.write(event,event.getRequest().getParams().toString(), ContentType.TEXT_PLAIN.toString());
    console.info("Http Server Get:"+event.getRequest().getParams().toString());
});

HttpServer.setRoot("/hutool")//设置简单文件服务器路径，即定义一个root目录，则请求路径后直接访问目录下的资源，默认请求index.html，类似于Nginx。

HttpServer.start();//启动服务器

HttpServer.close();//关闭HttpServer并清理空间

//请求事件的文件上传版本
HttpServerEvents.path("/test",event=>{
    var file = event.getRequest().getMultipart().getFile("file");
    file.write("d:/test/");//传入目录，默认读取HTTP头中的文件名然后创建文件
    HttpServer.write(event,"OK!", ContentType.TEXT_PLAIN.toString());
});

var runtime =  HttpServer.getRuntimeLocation();//获取当前运行位置，实际上就是MC服务器的根目录（绝对位置）

```

### HttpClient
HttpClient的使用很简单
```javascript
var args = new Map();//创建一个Map存放参数
args.set("first","first_args");

HttpClient.get("https://www.baidu.com");//get获取百度首页

HttpClient.get("https://www.baidu.com",args);//带参数的get百度首页

HttpClient.post("http://127.0.0.1:8620",args);//post请求一个地址

HttpClient.downloadString("https://www.baidu.com","utf-8");//下载百度首页为字符串，utf-8编码

HttpClient.download("https://www.baidu.com","/down");//下载文件， 目标文件或目录，第二个参数当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名

HttpClient.setMaxRedirectCount(10);//设置允许重定向的次数

HttpClient.isHttps("https://baidu.com")//判断一个url是否为https请求

```
### WebSocket
#### 通用方法
```javascript
WebSocket.creatConnect("ws://127.0.0.1:8620");//创建websocket连接对象
WebSocket.connect();//启动连接
WebSocket.sendMessage();//发送信息，这个方法是线程安全的
WebSocket.close();//关闭WebSocket连接，并清理环境
```
#### 事件
```javascript
//用法
WebSocketEvents.event("event_name",event=>{});
```
#### 事件列表
1. onClose WebSocket连接关闭事件
2. onError WebSocket连接错误事件
3. onMessage WebSocket收到信息事件
4. onOpen WebSocket连接启动事件
#### event内容
使用getter方法进行访问
```java
protected String message;
protected ServerHandshake serverHandshake;
protected int code;
protected String reason;
protected boolean remote;
```

### MQTT
tcp连接支持两种类型的连接：  
TCP连接：tcp://  
ssl/TLS保护的tcp连接：ssl://  
例如：   
tcp://localhost:1883   
ssl://localhost:8883   
如果未指定端口，则：  
tcp：//“URI默认为1883  
ssl://URI默认为8883  
#### 通用方法
```javascript
MQTT.creatConnect("broker","clientId");//创建MQTT连接对象，broker是MQTTserverURI
MQTT.setServerURIs(["URI1","URI2"]);//设置多个MQTT服务器
MQTT.setCleanSession(true);//设置CleanSession
MQTT.setPassword("password");//设置密码
MQTT.setUsername("username");//设置用户名
MQTT.setAutomaticReconnect(true);//设置自动重连
MQTT.setMaxInflight(10);//设置最大等待数据
MQTT.setKeepalive(10);//设置Keepalive
MQTT.setWillPayloadAndTopic(2,"topic","payload",false);//设置服务质量(QOS),主题,负载,retain
MQTT.connect();//启动连接
MQTT.disconnect();//关闭连接

```
#### 事件
```javascript
MQTTEvents.event("event_name",()=>{});
```
#### 事件列表
1. disconnected 连接关闭事件
2. mqttErrorOccurred 连接错误事件
3. messageArrived 收到订阅信息事件
4. deliveryComplete 交付完成事件
5. connectComplete 连接完成事件
6. authPacketArrived 身份验证数据包到达事件

#### event内容
使用getter方法进行访问
```java
MqttDisconnectResponse disconnectResponse;
String topic;
MqttMessage message;
IMqttToken token;
boolean reconnect;
String serverURI;
int reasonCode;
MqttProperties properties;
```

### FileWriter
```javascript
var writer = FileWriter.create("test.properties");
writer.write("test");
```

写入文件分为追加模式和覆盖模式两类，追加模式可以用`append`方法，覆盖模式可以用`write`方法，同时也提供了一个write方法，第二个参数是可选覆盖模式。

同样，此类提供了：
- `getOutputStream`
- `getWriter`
- `getPrintWriter`

### FileReader
```javascript
//默认UTF-8编码，可以在构造中传入第二个参数做为编码
var fileReader = FileReader.create("test.properties");
var result = fileReader.readString();
```

FileReader提供了以下方法来快速读取文件内容：

- `readBytes`
- `readString`
- `readLines`

同时，此类还提供了以下方法用于转换为流或者BufferedReader：
- `getReader`
- `getInputStream`

### ReUtil
正则表达式支持
#### ReUtil.extractMulti
抽取多个分组然后把它们拼接起来

```javascript
var content = "ZZZaaabbbccc中文1234";
var resultExtractMulti = ReUtil.extractMulti("(\\w)aa(\\w)", content, "$1-$2");
Assert.assertEquals("Z-a", resultExtractMulti);
```

#### ReUtil.delFirst
删除第一个匹配到的内容

```javascript
var content = "ZZZaaabbbccc中文1234";
var resultDelFirst = ReUtil.delFirst("(\\w)aa(\\w)", content);
Assert.assertEquals("ZZbbbccc中文1234", resultDelFirst);
```

#### ReUtil.findAll
查找所有匹配文本

```javascript
var content = "ZZZaaabbbccc中文1234";
var resultFindAll = ReUtil.findAll("\\w{2}", content, 0, new ArrayList<String>());
// 结果：["ZZ", "Za", "aa", "bb", "bc", "cc", "12", "34"]
```

#### ReUtil.getFirstNumber
找到匹配的第一个数字

```javascript
var resultGetFirstNumber = ReUtil.getFirstNumber(content);
// 结果：1234
```

#### ReUtil.isMatch
给定字符串是否匹配给定正则

```javascript
var content = "ZZZaaabbbccc中文1234";
var isMatch = ReUtil.isMatch("\\w+[\u4E00-\u9FFF]+\\d+", content);
Assert.assertTrue(isMatch);
```

#### ReUtil.replaceAll
通过正则查找到字符串，然后把匹配到的字符串加入到replacementTemplate中，$1表示分组1的字符串

```javascript
var content = "ZZZaaabbbccc中文1234";
//此处把1234替换为 ->1234<-
var replaceAll = ReUtil.replaceAll(content, "(\\d+)", "->$1<-");
Assert.assertEquals("ZZZaaabbbccc中文->1234<-", replaceAll);
```

#### ReUtil.escape
转义给定字符串，为正则相关的特殊符号转义

```javascript
var escape = ReUtil.escape("我有个$符号{}");
// 结果：我有个\\$符号\\{\\}
```
### HtmlUtil
针对Http请求中返回的Http内容，Hutool使用此工具类来处理一些HTML页面相关的事情。

比如我们在使用爬虫爬取HTML页面后，需要对返回页面的HTML内容做一定处理，比如去掉指定标签（例如广告栏等）、去除JS、去掉样式等等，这些操作都可以使用**HtmlUtil**完成。

#### `HtmlUtil.escape`

转义HTML特殊字符，包括：

1. `'` 替换为 `&#039;`
2. `"` 替换为 `&quot;`
3. `&` 替换为 `&amp;`
4. `<` 替换为 `&lt;`
5. `>` 替换为 `&gt;`

```javascript
var html = "<html><body>123'123'</body></html>";
// 结果为：&lt;html&gt;&lt;body&gt;123&#039;123&#039;&lt;/body&gt;&lt;/html&gt;
var escape = HtmlUtil.escape(html);
```


####  `HtmlUtil.unescape`

还原被转义的HTML特殊字符

```javascript
var escape = "&lt;html&gt;&lt;body&gt;123&#039;123&#039;&lt;/body&gt;&lt;/html&gt;";
// 结果为：<html><body>123'123'</body></html>
var unescape = HtmlUtil.unescape(escape);
```

####  `HtmlUtil.removeHtmlTag`

清除指定HTML标签和被标签包围的内容

```javascript
var str = "pre<img src=\"xxx/dfdsfds/test.jpg\">";
// 结果为：pre
var result = HtmlUtil.removeHtmlTag(str, "img");
```

####  `HtmlUtil.cleanHtmlTag`

清除所有HTML标签，但是保留标签内的内容

```javascript
var str = "pre<div class=\"test_div\">\r\n\t\tdfdsfdsfdsf\r\n</div><div class=\"test_div\">BBBB</div>";
// 结果为：pre\r\n\t\tdfdsfdsfdsf\r\nBBBB
var result = HtmlUtil.cleanHtmlTag(str);
```

####  `HtmlUtil.unwrapHtmlTag`

清除指定HTML标签，不包括内容

```javascript
var str = "pre<div class=\"test_div\">abc</div>";
// 结果为：preabc
var result = HtmlUtil.unwrapHtmlTag(str, "div");
```

#### `HtmlUtil.removeHtmlAttr`

去除HTML标签中的指定属性，如果多个标签有相同属性，都去除

```javascript
var html = "<div class=\"test_div\"></div><span class=\"test_div\"></span>";
// 结果为：<div></div><span></span>
var result = HtmlUtil.removeHtmlAttr(html, "class");
```

####  `HtmlUtil.removeAllHtmlAttr`

去除指定标签的所有属性

```javascript
var html = "<div class=\"test_div\" width=\"120\"></div>";
// 结果为：<div></div>
var result = HtmlUtil.removeAllHtmlAttr(html, "div");
```

####  `HtmlUtil.filter` 过滤HTML文本，防止XSS攻击

```javascript
var html = "<alert></alert>";
// 结果为：""
var filter = HtmlUtil.filter(html);
```
### ThreadUtil

在KubeJS中，可以使用箭头函数语法使用Runnable接口：
```javascript
ThreadUtil.execute(()=>{
    //do something
});
```

#### `ThreadUtil.execute`

直接在公共线程池中执行线程

#### `ThreadUtil.newExecutor`

获得一个新的线程池

#### `ThreadUtil.execAsync`

执行异步方法

#### `ThreadUtil.newCompletionService`

创建CompletionService，调用其submit方法可以异步执行多个任务，最后调用take方法按照完成的顺序获得其结果。若未完成，则会阻塞。

#### `ThreadUtil.newCountDownLatch`

新建一个CountDownLatch，一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。

#### `ThreadUtil.sleep`

挂起当前线程，是`Thread.sleep`的封装，通过返回boolean值表示是否被打断，而不是抛出异常。

> `ThreadUtil.safeSleep`方法是一个保证挂起足够时间的方法，当给定一个挂起时间，使用此方法可以保证挂起的时间大于或等于给定时间，解决`Thread.sleep`挂起时间不足问题，此方法在Hutool-cron的定时器中使用保证定时任务执行的准确性。

#### `ThreadUtil.getStackTrace`

此部分包括两个方法：

- `getStackTrace` 获得堆栈列表
- `getStackTraceElement` 获得堆栈项

#### 其它

- `createThreadLocal` 创建本地线程对象
- `interupt` 结束线程，调用此方法后，线程将抛出InterruptedException异常
- `waitForDie` 等待线程结束. 调用 `Thread.join()` 并忽略 InterruptedException
- `getThreads` 获取JVM中与当前线程同组的所有线程
- `getMainThread` 获取进程的主线程

