# 使用 Thrift

教程：https://www.baeldung.com/apache-thrift

Thrift 使用一种特殊的接口描述语言 (IDL)
来定义数据类型和服务接口，这些数据类型和服务接口存储为 `.thrift`
文件，稍后用作编译器的输入，用于生成通过不同编程语言进行通信的客户端和服务器软件的源代码。

## 安装

`brew install thrift`

- 命令行

```
cd path/to/thrift
thrift -r --gen [LANGUAGE] [FILENAME]
```

- Maven 依赖

```
<dependency>
    <groupId>org.apache.thrift</groupId>
    <artifactId>libthrift</artifactId>
    <version>0.17.0</version>
</dependency>
```

- Maven 插件

```
  <build>
    <defaultGoal>install</defaultGoal>
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>build-helper-maven-plugin</artifactId>
        <version>3.3.0</version>
        <executions>
          <execution>
            <phase>generate-sources</phase>
            <goals>
              <goal>add-source</goal>
            </goals>
            <configuration>
              <sources>
                <source>generated</source>
              </sources>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
```

- 然后执行 `mvn clean install`

## 生成代码

- 定义 Thrift 文件，在 `src/main/resources` 下添加 `service.thrift` 文件
    - 定义了一组命名空间、一个异常类型、一个结构，最后是一个服务接口

```
namespace cpp a.b.c.impl
namespace java a.b.c.impl

exception InvalidOperationException {
    1: i32 code,
    2: string description
}

struct CrossPlatformResource {
    1: i32 id,
    2: string name,
    3: optional string salutation
}

service CrossPlatformService {

    CrossPlatformResource get(1:i32 id) throws (1:InvalidOperationException e),

    void save(1:CrossPlatformResource resource) throws (1:InvalidOperationException e),

    list <CrossPlatformResource> getList() throws (1:InvalidOperationException e),

    bool ping() throws (1:InvalidOperationException e)
}
```

- 运行 `thrift -r -gen java src/main/resources/service.thrift` 生成大约以下代码
    - 我们需要实现 `CrossPlatformService.Iface` 接口
    - `gen-java/a/b/c/impl` 目录

```java
public class CrossPlatformService {
  public interface Iface {
    public CrossPlatformResource get(int id)
        throws InvalidOperationException, org.apache.thrift.TException;

    public void save(CrossPlatformResource resource)
        throws InvalidOperationException, org.apache.thrift.TException;

    public java.util.List<CrossPlatformResource> getList()
        throws InvalidOperationException, org.apache.thrift.TException;

    public boolean ping() throws InvalidOperationException, org.apache.thrift.TException;
  }
}
```

### 生成的代码还需要添加相关的依赖，比如 `javax.annotation` `org.slf4j` 等

## 服务实现

实现 `CrossPlatformService.Iface` 接口

```java
public class CrossPlatformServiceImpl implements CrossPlatformService.Iface {
  @Override
  public CrossPlatformResource get(final int id)
      throws InvalidOperationException, TException {
    return new CrossPlatformResource();
  }

  @Override
  public void save(final CrossPlatformResource resource)
      throws InvalidOperationException, TException {

  }

  @Override
  public List<CrossPlatformResource> getList()
      throws InvalidOperationException, TException {
    return Collections.emptyList();
  }

  @Override
  public boolean ping() throws InvalidOperationException, TException {
    return true;
  }
}
```

## 服务端

```java
public class CrossPlatformServiceServer {
  private TServer server;

  public void start() throws TTransportException {
    // 定义一个ServerTransport，用于监听客户端的连接
    TServerTransport serverTransport = new TServerSocket(9999);

    // TSimpleServer 用于单线程的服务模型，一般用于测试
    // processor 用于处理客户端的请求
    server = new TSimpleServer(new TServer.Args(serverTransport).processor(
        new CrossPlatformService.Processor<>(new CrossPlatformServiceImpl())));
    System.out.print("Starting the server... ");

    // 启动服务
    server.serve();
    System.out.println("done.");
  }

  public void stop() {
    if (server != null && server.isServing()) {
      System.out.print("Stopping the server... ");

      server.stop();
      System.out.println("done.");
    }
  }
}
```

## 客户端

```java
public class CrossPlatformServiceClient {
  public boolean ping() {
    try {
      TTransport transport;

      transport = new TSocket("localhost", 9999);
      transport.open();

      TProtocol protocol = new TBinaryProtocol(transport);
      CrossPlatformService.Client client = new CrossPlatformService.Client(protocol);

      boolean result = client.ping();

      System.out.println("done.");
      transport.close();

      return result;
    } catch (TTransportException e) {
      e.printStackTrace();
    } catch (TException x) {
      x.printStackTrace();
    }
    return false;
  }
}
```