package equus.webstack.persist.module;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;

public class PersistenceModule extends AbstractModule {
  public static final String JPA_UNIT = "webStack";

  @Override
  protected void configure() {
    install(new JpaPersistModule(JPA_UNIT));
  }
}
