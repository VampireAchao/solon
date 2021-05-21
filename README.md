
[![Maven Central](https://img.shields.io/maven-central/v/org.noear/solon.svg)](https://mvnrepository.com/search?q=g:org.noear%20AND%20solon)

` QQ交流群：22200020 `

# Solon for java

A plug-in Java micro-development framework。

Support jdk8 +; Core frame 0.1Mb; Combining different plug-ins to meet different needs; Convenient customization; Rapid development of。

* Restrained, concise and open
* Faster, smaller, freer
* Unified development experience of HTTP, WebSocket and Socket signals (commonly known as three sources in one)
* Support annotation and manual two modes, free control as needed
* Not Servlets, which can be adapted to any underlying communication framework (so: RPC architecture runs at minimum 0.2Mb)
* Self-built IOC & AOP container, support REST API, MVC, Job, Remoting, MicoService and other development
* Set Handler + Context and Listener + Message architecture patterns; Emphasis on plug-in extension; Adapt to different application scenarios
* Plug-ins are extensible and switchable: startup plug-in, extension plug-in, serialization plug-in, data plug-in, session state plug-in, view plug-in (coexist), etc.
* The use of Spring Boot feels similar to the migration cost is low


### Hello world：

```java
//Handler mode：
public class App{
    public static void main(String[] args){
        SolonApp app = Solon.start(App.class,args);
        
        app.get("/",(c)->c.output("Hello world!"));
    }
}

//Controller mode：
@Controller
public class App{
    public static void main(String[] args){
        Solon.start(App.class,args);
    }
  
    @Mapping("/")
    public Object home(Context c){
        return "Hello world!";  
    }
}
```


### Main framework and rapid integration development package：

###### Main frame

| component | description |
| --- | --- |
| org.noear:solon-parent | Framework versioning |
| org.noear:solon | Main frame |
| org.noear:nami | Companion Framework (as a client to Solon Remoting) |

###### Rapid integration of development kits

| component | description |
| --- | --- |
| org.noear:solon-lib | Rapid development of basic integration packages |
| org.noear:solon-api | solon-lib + http boot；Rapid development of interface applications |
| org.noear:solon-web | solon-api + freemarker + sessionstate；Rapid development of WEB applications |
| org.noear:solon-rpc | solon-api + nami；Rapid development of remoting applications |
| org.noear:solon-cloud | solon-rpc + consul；Rapid development of microservice applications |

### Attachment 1: A quick understanding of Solon's materials：

#### [《What is the difference between Solon and SpringBoot?》](https://my.oschina.net/noear/blog/4863844)

#### [《Solon Cloud distributed service development suite manifest, feel different from Spring Cloud》](https://my.oschina.net/noear/blog/5039169)

#### [《Solon's ideas and architecture notes》](https://my.oschina.net/noear/blog/4980834)

#### [《Solon Ecological Plugins List》](https://my.oschina.net/noear/blog/5053423)

#### [《Introduction to the Solon framework》](https://my.oschina.net/noear/blog/4784513)


### Attachment 2: Examples and articles
* Within the project：[_test](./_test/) 和 [_demo](./_demo/)
* More examples：[solon_demo](https://gitee.com/noear/solon_demo) 、 [solon_rpc_demo](https://gitee.com/noear/solon_rpc_demo) 、 [solon_socketd_demo](https://gitee.com/noear/solon_socketd_demo) 、 [solon_cloud_demo](https://gitee.com/noear/solon_cloud_demo)
* More articles：[https://www.cnblogs.com/noear/](https://www.cnblogs.com/noear/)

### Attachment 3: Quick Start Examples
* Web example（mvc）
```xml
<parent>
    <groupId>org.noear</groupId>
    <artifactId>solon-parent</artifactId>
    <version>1.4.2</version>
</parent>

<dependencies>
    <dependency>
        <groupId>org.noear</groupId>
        <artifactId>solon-web</artifactId>
    </dependency>
</dependencies>

```
```
//资源路径说明（不用配置）
resources/application.properties（或 application.yml） 为应用配置文件
resources/static/ 为静态文件根目标
resources/WEB-INF/view/ 为视图文件根目标（支持多视图共存）

//调试模式：
启动参数添加：-debug=1
```
```java
public class App{
    public static void main(String[] args){
        Solon.start(App.class, args);
    }
}

/*
 * mvc控制器
 */
@Controller
public class DemoController{
    //for http
    @Mapping("/hallo/{u_u}")
    public ModelAndView hallo(String u_u){
        return new ModelAndView("hallo");
    }
    
    /*
    //for web socket （需添加：solon.boot.websocket 插件）
    @Mapping(value="/hallo/{u_u}", method = MethodType.WEBSOCKET)
    public ModelAndView hallo_ws(String u_u){
        return new ModelAndView("hallo");
    }
    */
}
```

* Remoting example（rpc）

```java
// - interface : 定义协议
public interface DemoService{
    void setName(Integer user_id, String name);
}

// - server : 实现协议
@Mapping("/demo/*")
@Remoting
public class DemoServiceImp implements DemoService{
    public void setName(int user_id, String name){
        
    }
}

// - client - 简单示例
//注入模式
//@NamiClient("http://127.0.0.1:8080/demo/") 
//DemoService client;

//构建模式
DemoService client = Nami.builder().upstream(n->"http://127.0.0.1:8080/demo/").create(DemoService.class); 
client.setName(1,'');


```

* Get the application configuration
```java
//非注入模式
Solon.cfg().get("app_key"); //=>String
Solon.cfg().getInt("app_id",0); //=>int
Solon.cfg().getProp("xxx.datasource"); //=>Properties

//注入模式
@Configuration //or @Controller, or @Component
public class Config{
    @Inject("${app_key}")
    String app_key;

    @Inject("${app_title:Solon}")
    String app_title;
}
```

* Transaction and cache control (+ validation)
```java
@Valid
@Controller
public class DemoController{
    @Db
    BaseMapper<UserModel> userService;
    
    @NotZero("user_id")
    @CacheRemove(tags = "user_${user_id}")
    @Tran
    @Mapping("/user/update")
    public void udpUser(int user_id, UserModel user){
        userService.updateById(user);
    }

    @NotZero("user_id")
    @Cache(tags = "user_${user_id}")
    public UserModel getUser(int user_id){
        return userService.selectById(user_id);
    }
}
```

* File upload and output
```java
@Controller
public class DemoController{
    @Mapping("/file/upload")
    public void upload(UploadedFile file){
        IoUtil.save(file.content, "/data/file_" + file.name);
    }

    @Mapping("/file/down")
    public void down(Context ctx, String path){
        URL uri = Utils.getResource(path);

        ctx.contentType("json/text");
        ctx.output(uri.openStream());
    }
}
```

* Servlet annotations are still supported
```java
@WebFilter("/hello/*")
public class HelloFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletResponse.getWriter().write("Hello，我把你过滤了");
    }
}
```

* Quartz 定时任务
```java
@Quartz(cron7x = "0 0/1 * * * ? *")
public class HelloTask implements Runnable {
    public static void main(String[] args){
        Solon.start(QuartzRun2.class,args);
    }
    
    @Override
    public void run() {
        System.out.println("Hello world");
    }
}
```

* 体外扩展加载 jar
```
demoApp.jar             #主程序
ext/                    #扩展目录
ext/ext.markdown.jar    #MD格式支持扩展包
```

* 单链接双向RPC（客户端链上服务端之后，形成双向RPC）
```java 
//server
@Mapping(value = "/demoh/rpc", method = MethodType.SOCKET)
@Remoting
public class HelloRpcServiceImpl implements HelloRpcService {
    public String hello(String name) {
        //此处，可以根据 client session 创建一个连接 client 的 rpc service
        NameRpcService rpc = SocketD.create(Context.current(), NameRpcService.class);

        String name2 = rpc.name(name);

        return "name=" + name;
    }
}

//client
HelloRpcService rpc = SocketD.create("tcp://localhost:"+_port, HelloRpcService.class);

String rst = rpc.hello("noear");
```

* Solon cloud 配置服务使用
```java
@Controller
public class DemoController {
    public static void main(String[] args){
        Solon.start(DemoController.class,args);
    }
    
    //注入模式
    @CloudConfig(value = "user.name", autoRefreshed = true)
    String userName;
    
    @Mapping("/")
    public void run() {
        //手动获取模式
        userName = CloudClient.config().pull("user.name").value();
    }
}
```

* Solon cloud 事件总线使用
```java
//事件订阅与消费
@CloudEvent("hello.demo")
public class DemoEvent implements CloudEventHandler {
    @Override
    public boolean handler(Event event) throws Throwable {
        //返回成功
        return true;
    }
}

//事件产生
CloudClient.event().publish(new Event("hello.demo", msg));
```


### 附4：插件开发说明
* 新建一个 maven 项目
* 新建一个 java/{包名}/XPluginImp.java （implements XPlugin）
* 新建一个 resources/META-INF/solon/{包名.properties}
*    添加配置：solon.plugin={包名}.XPluginImp

### 附5：启动顺序参考

* 1.实例化 Solon.global() 并加载配置
* 2.加载扩展文件夹
* 3.扫描插件并排序
* 4.运行 initialize 函数
* 5.推送 AppInitEndEvent [事件]
* 6.运行插件
* 7.推送 PluginLoadEndEvent [事件]
* 8.导入java bean(@Import)
* 9.扫描并加载java bean
* a.推送 BeanLoadEndEvent [事件]
* b.加载渲染印映关系
* c.执行bean加完成事件
* d.推送 AppLoadEndEvent [事件]
* e.结束


### 附6：Helloworld 的单机并发数 [《helloworld_wrk_test》](https://gitee.com/noear/helloworld_wrk_test)

> * 机器：2017 macbook pro 13, i7, 16g, MacOS 10.15, jdk11
> * 测试：wrk -t10 -c200 -d30s --latency "http://127.0.0.1:8080/"

|  solon 1.1.2 | 大小 | QPS | 
| -------- | -------- | -------- | 
| solon.boot.jlhttp(bio)     | 0.1m     | 4.7万左右     |
| solon.boot.jetty(nio, 支持servlet api)     | 1.8m     | 10.7万左右     | 
| solon.boot.undertow(nio, 支持servlet api)     | 4.2m     | 11.3万左右     | 
| solon.boot.smarthttp(aio)     | 0.3m     | 12.4万左右     | 


| spring boot 2.3.3  | 大小 |  QPS  | 
| -------- | -------- | -------- |
| spring-boot-starter-tomcat   | 16.1m |  3.2万左右  | 
| spring-boot-starter-jetty | 16m | 3.7万左右 |
| spring-boot-starter-undertow | 16.8m | 4.4万左右 |
