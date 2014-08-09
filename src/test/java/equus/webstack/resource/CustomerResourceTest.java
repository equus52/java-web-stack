package equus.webstack.resource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import equus.webstack.application.JettyStarter;
import equus.webstack.application.WebStackApplication;
import equus.webstack.application.module.TestModules;

public class CustomerResourceTest {
  private Server server;
  private WebTarget target;

  @Before
  public void before() throws Exception {
    WebStackApplication.setInjector(TestModules.createWebMockInjector());
    JettyStarter starter = new JettyStarter();
    server = starter.startServer();

    Client client = JerseyClientBuilder.createClient();
    target = client.target(starter.getAPIBaseURI());
  }

  @After
  public void after() throws Exception {
    if (server != null) {
      server.stop();
    }
  }

  @Test
  public void test_update() throws Exception {

    String entity = target.path("customers").path("2").request(MediaType.APPLICATION_JSON_TYPE).get()
        .readEntity(String.class);
    Response resp = target.path("customers").request(MediaType.APPLICATION_JSON_TYPE)
        .put(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE));
    String json = resp.readEntity(String.class);

    System.out.println(entity);
    System.out.println(json);
  }

  @Test
  public void test_delete() throws Exception {

    String entity = target.path("customers").path("2").request(MediaType.APPLICATION_JSON_TYPE).get()
        .readEntity(String.class);
    Response resp = target.path("customers").path("2").queryParam("version", 13)
        .request(MediaType.APPLICATION_JSON_TYPE).delete();
    String json = resp.readEntity(String.class);

    System.out.println(entity);
    System.out.println(json);
  }

  @Test
  public void test_post() throws Exception {

    String entity = "{\"name\":\"new\"}";
    Response resp = target.path("customers").request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE));
    String json = resp.readEntity(String.class);

    System.out.println(entity);
    System.out.println(json);
  }
}