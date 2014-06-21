package equus.webstack.service.module;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;

import equus.webstack.service.CustomerService;
import equus.webstack.service.CustomerServiceImpl;
import equus.webstack.service.OrderService;
import equus.webstack.service.OrderServiceImpl;

public class ServiceModule extends AbstractModule {

  public static final String JPA_UNIT = "webStack";

  @Override
  protected void configure() {
    install(new JpaPersistModule(JPA_UNIT));
    bind(CustomerService.class).to(CustomerServiceImpl.class);
    bind(OrderService.class).to(OrderServiceImpl.class);
  }
}
