package equus.webstack.command;

import java.util.Properties;

import lombok.val;

import com.google.inject.Guice;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

import equus.webstack.service.module.ServiceModule;

public class DBDropCreateCommand {
  public static void main(String[] args) {
    new DBDropCreateCommand().execute();
  }

  public void execute() {
    JpaPersistModule module = new JpaPersistModule(ServiceModule.JPA_UNIT);
    Properties properties = new Properties();
    properties.setProperty("hibernate.hbm2ddl.auto", "create");
    module.properties(properties);
    val injector = Guice.createInjector(module);
    val persistService = injector.getInstance(PersistService.class);
    persistService.start();
    persistService.stop();
  }
}
