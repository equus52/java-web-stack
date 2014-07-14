package equus.webstack.application;

import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

@Slf4j
public class JettyStarter {
  private final int port = 9010;
  private final String contextPath = "/java-web-stack/";
  private String resourceBase = "WebContent";

  public static void main(String[] args) {
    JettyStarter jetty = new JettyStarter();
    if (args.length > 0) {
      jetty.resourceBase = args[0];
    }
    jetty.start();
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
    System.out.println("URL http://localhost:" + port + contextPath);
    server.join();
  }

  private Server createServer() {
    val server = new Server(port);
    val context = new WebAppContext();
    context.setServer(server);
    context.setContextPath(contextPath);
    context.setDescriptor("WebContent/WEB-INF/web.xml");
    context.setResourceBase(resourceBase);
    context.setParentLoaderPriority(true);
    server.setHandler(context);
    return server;
  }
}
