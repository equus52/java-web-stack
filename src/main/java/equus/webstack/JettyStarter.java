package equus.webstack;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

@Slf4j
public class JettyStarter {
  private static final int port = 9010;

  public static void main(String[] args) {
    new JettyStarter().start();
  }

  @SneakyThrows
  public void start() {
    Server server = createServer();

    Thread shutdownHook = new Thread(() -> {
      try {
        server.stop();
      } catch (Throwable t) {
        log.error("unknown error occurred.", t);
      }
    }, "shutdown-hook");
    Runtime.getRuntime().addShutdownHook(shutdownHook);

    server.start();
    server.join();
  }

  private Server createServer() {
    Server server = new Server(port);
    WebAppContext context = new WebAppContext();
    context.setServer(server);
    context.setContextPath("/");
    context.setDescriptor("src/main/webapp/WEB-INF/web.xml");
    context.setResourceBase("src/main/webapp/");
    context.setParentLoaderPriority(true);
    server.setHandler(context);
    return server;
  }
}
