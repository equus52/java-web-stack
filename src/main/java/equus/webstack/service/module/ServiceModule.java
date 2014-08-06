package equus.webstack.service.module;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import equus.webstack.service.CustomerService;
import equus.webstack.service.CustomerServiceImpl;
import equus.webstack.service.OrderService;
import equus.webstack.service.OrderServiceImpl;

public class ServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bindInterceptor(Matchers.subclassesOf(PersistenceServiceInterceptor.TARGET_CLASS), Matchers.any(),
        new PersistenceServiceInterceptor());

    bind(CustomerService.class).to(CustomerServiceImpl.class);
    bind(OrderService.class).to(OrderServiceImpl.class);
  }
}
