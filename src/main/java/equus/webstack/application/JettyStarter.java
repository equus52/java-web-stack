package equus.webstack.application;

import lombok.SneakyThrows;
import lombok.val;
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
    val server = createServer();

    val shutdownHook = new Thread(() -> {
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
    val server = new Server(port);
    val context = new WebAppContext();
    context.setServer(server);
    context.setContextPath("/");
    context.setDescriptor("src/main/webapp/WEB-INF/web.xml");
    context.setResourceBase("src/main/webapp/");
    context.setParentLoaderPriority(true);
    server.setHandler(context);
    return server;
  }
}
