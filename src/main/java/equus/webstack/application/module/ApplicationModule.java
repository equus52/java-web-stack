package equus.webstack.application.module;

import com.google.inject.AbstractModule;

import equus.webstack.persist.module.PersistenceModule;
import equus.webstack.service.module.ServiceModule;

public class ApplicationModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new ServiceModule());
    install(new PersistenceModule());
  }

}
