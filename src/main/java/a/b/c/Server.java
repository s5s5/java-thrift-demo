package a.b.c;

import org.apache.thrift.transport.TTransportException;

public class Server {
  public static void main(String[] args) {
    CrossPlatformServiceServer server = new CrossPlatformServiceServer();
    try {
      server.start();
    } catch (TTransportException e) {
      e.printStackTrace();
    }
  }
}
