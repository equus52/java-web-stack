package equus.webstack.command;

import lombok.val;

import com.google.inject.Injector;

import equus.webstack.model.Customer;
import equus.webstack.service.CustomerService;

public class DBInitializeWithTestDataCommand {

  public static void main(String[] args) {
    new DBInitializeWithTestDataCommand().execute();
  }

  public void execute() {
    new DBInitializeCommand().execute(injector -> {
      initCustomer(injector);
      // setup test data
      });
  }

  private void initCustomer(Injector injector) {
    val service = injector.getInstance(CustomerService.class);
    val customer = new Customer();
    customer.setId(0);
    customer.setName("Hiro");
    service.save(customer);

  }

}
