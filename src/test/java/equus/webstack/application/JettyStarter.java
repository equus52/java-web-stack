package equus.webstack.application;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

@Slf4j
public class JettyStarter {
  private final int port = 9010;
  private final String contextPath = "/java-web-stack/";
  private final String apiPath = "api";
  @Setter
  private boolean resourceEnable = true;
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
    Server server = startServer();
    server.join();
  }

  @SneakyThrows
  public Server startServer() {
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
    if (resourceEnable) {
      System.out.println("URL " + getBaseURI());
    }
    System.out.println("API URL " + getAPIBaseURI());
    return server;
  }

  public URI getBaseURI() {
    return UriBuilder.fromUri("http://localhost/").port(port).path(contextPath).build();
  }

  public URI getAPIBaseURI() {
    return UriBuilder.fromUri("http://localhost/").port(port).path(contextPath).path(apiPath).build();
  }

  private Server createServer() {
    val server = new Server(port);
    val context = new WebAppContext();
    context.setServer(server);
    context.setContextPath(contextPath);
    context.setDescriptor("WebContent/WEB-INF/web.xml");
    context.setParentLoaderPriority(true);
    if (resourceEnable) {
      context.setResourceBase(resourceBase);
    }
    server.setHandler(context);
    return server;
  }
}
