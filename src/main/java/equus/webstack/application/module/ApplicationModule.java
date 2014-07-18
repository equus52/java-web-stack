package equus.webstack.application.module;

import com.google.inject.AbstractModule;

import equus.webstack.persist.module.PersistModule;
import equus.webstack.service.module.ServiceModule;

public class ApplicationModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new PersistModule());
    install(new ServiceModule());
  }

}
