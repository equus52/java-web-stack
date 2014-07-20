package equus.webstack.command;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.Target;
import org.slf4j.Logger;

import equus.webstack.persist.configuration.CustomConfiguration;

@Slf4j
public class DBDropCreateCommand implements Command {
  @Override
  public Logger getLogger() {
    return log;
  }

  public static void main(String[] args) {
    new DBDropCreateCommand().executeCommand();
  }

  @Override
  public void execute(String... args) {

    CustomConfiguration config = CustomConfiguration.generateConfiguration();

    SchemaExport schemaExport = new SchemaExport(config);
    schemaExport.setOutputFile(GenerateDDLCommand.DROP_CREATE_PATH);
    schemaExport.create(Target.BOTH);
    // JpaPersistModule module = new JpaPersistModule(PersistModule.JPA_UNIT);
    // Properties properties = new Properties();
    // properties.setProperty("hibernate.hbm2ddl.auto", "create");
    // module.properties(properties);
    // val injector = Guice.createInjector(module);
    // val persistService = injector.getInstance(PersistService.class);
    // persistService.start();
    // persistService.stop();
  }

}
