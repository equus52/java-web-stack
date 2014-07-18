package equus.webstack.command;

import java.util.Properties;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;

import com.google.inject.Guice;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

import equus.webstack.persist.module.PersistModule;

@Slf4j
public class DBDropCreateCommand implements Command {
  @Override
  public Logger getLogger() {
    return log;
  }

  @Override
  public String getName() {
    return this.getClass().getName();
  }

  public static void main(String[] args) {
    new DBDropCreateCommand().executeCommand();
  }

  @Override
  public void execute(String... args) {
    JpaPersistModule module = new JpaPersistModule(PersistModule.JPA_UNIT);
    Properties properties = new Properties();
    properties.setProperty("hibernate.hbm2ddl.auto", "create");
    module.properties(properties);
    val injector = Guice.createInjector(module);
    val persistService = injector.getInstance(PersistService.class);
    persistService.start();
    persistService.stop();
  }

}
