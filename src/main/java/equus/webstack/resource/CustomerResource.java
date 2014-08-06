package equus.webstack.resource;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.RequiredArgsConstructor;
import lombok.val;
import equus.webstack.model.Customer;
import equus.webstack.service.CustomerService;

@Path("customers")
@RequiredArgsConstructor(onConstructor = @__({ @Inject }))
public class CustomerResource {

  private final CustomerService customerService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Customer> findAll() {
    boolean test = false;
    if (test) {
      val customer = customerService.findByPrimaryKey(1);
      customer.setName("BBB");
      customerService.update(customer);
    }

    return customerService.findAll();
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
