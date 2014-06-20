package equus.webstack.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import equus.webstack.model.User;

@Path("users")
public class UserResource {
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public User getHello() {
    User user = new User();
    user.setId(0);
    user.setName("Hiro");
    return user;
  }
}
