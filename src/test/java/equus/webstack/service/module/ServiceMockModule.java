package equus.webstack.service.module;

import static org.mockito.Mockito.*;

import com.google.inject.AbstractModule;

import equus.webstack.service.CourseService;
import equus.webstack.service.CustomerService;
import equus.webstack.service.OrderService;
import equus.webstack.service.StudentService;

public class ServiceMockModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(CustomerService.class).toInstance(mock(CustomerService.class));
    bind(OrderService.class).toInstance(mock(OrderService.class));
    bind(StudentService.class).toInstance(mock(StudentService.class));
    bind(CourseService.class).toInstance(mock(CourseService.class));
  }

}
