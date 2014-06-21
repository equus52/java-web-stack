package equus.webstack.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.RequiredArgsConstructor;
import equus.webstack.model.Customer;
import equus.webstack.service.CustomerService;

@Path("customers")
@RequiredArgsConstructor(onConstructor = @__({ @Inject }))
public class CustomerResource {

  private final CustomerService customerService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Customer getHello() {
    Customer customer = new Customer();
    customer.setId(0);
    customer.setName("Hiro");
    return customer;
  }
}
