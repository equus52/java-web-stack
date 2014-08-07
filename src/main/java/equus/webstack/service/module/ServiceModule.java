package equus.webstack.service.module;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import equus.webstack.service.CourseService;
import equus.webstack.service.CourseServiceImpl;
import equus.webstack.service.CustomerService;
import equus.webstack.service.CustomerServiceImpl;
import equus.webstack.service.OrderService;
import equus.webstack.service.OrderServiceImpl;
import equus.webstack.service.StudentService;
import equus.webstack.service.StudentServiceImpl;

public class ServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bindInterceptor(Matchers.subclassesOf(PersistenceServiceInterceptor.TARGET_CLASS), Matchers.any(),
        new PersistenceServiceInterceptor());

    bind(CustomerService.class).to(CustomerServiceImpl.class);
    bind(OrderService.class).to(OrderServiceImpl.class);
    bind(StudentService.class).to(StudentServiceImpl.class);
    bind(CourseService.class).to(CourseServiceImpl.class);
  }
}
