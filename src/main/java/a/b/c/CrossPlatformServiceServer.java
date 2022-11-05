package a.b.c;

import a.b.c.impl.CrossPlatformService;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

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
