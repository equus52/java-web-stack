package equus.webstack.service.module;

import com.google.inject.AbstractModule;

import equus.webstack.service.CustomerService;
import equus.webstack.service.CustomerServiceImpl;
import equus.webstack.service.OrderService;
import equus.webstack.service.OrderServiceImpl;

public class ServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(CustomerService.class).to(CustomerServiceImpl.class);
    bind(OrderService.class).to(OrderServiceImpl.class);
  }
}
