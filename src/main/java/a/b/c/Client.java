package a.b.c;

public class Client {
  public static void main(String[] args) {
    CrossPlatformServiceClient client = new CrossPlatformServiceClient();
    System.out.println("ping() = " + client.ping());
  }
}
