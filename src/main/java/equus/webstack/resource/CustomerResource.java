package equus.webstack.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.RequiredArgsConstructor;
import lombok.val;
import equus.webstack.model.Customer;
import equus.webstack.service.CustomerService;
import equus.webstack.service.PersistenceService;

@Path("customers")
@RequiredArgsConstructor(onConstructor = @__({ @Inject }))
public class CustomerResource implements EntityResource<Customer> {

  private final CustomerService customerService;

  @Override
  public PersistenceService<Customer> getPersistenceService() {
    return customerService;
  }

  @GET
  @Path("/test")
  @Produces(MediaType.APPLICATION_JSON)
  public Customer getHello() {
    val customer = new Customer();
    customer.setId(0);
    customer.setName("Hiro");
    customerService.save(customer);
    return customerService.findByVersion(customer.getId(), customer.getVersion());
  }

}
