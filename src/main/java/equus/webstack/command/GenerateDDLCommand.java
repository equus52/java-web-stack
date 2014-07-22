package equus.webstack.command;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.Target;
import org.slf4j.Logger;

import equus.webstack.model.BaseEntity;
import equus.webstack.persist.configuration.CustomConfiguration;
import equus.webstack.persist.converter.LocalDateConverter;
import equus.webstack.persist.module.PersistenceModule;

@Slf4j
public class GenerateDDLCommand implements Command {
  public static final String DROP_CREATE_PATH = "ddl/drop_create.sql";

  @Override
  public Logger getLogger() {
    return log;
  }

  public static void main(String[] args) {
    new GenerateDDLCommand().execute();
  }

  public static CustomConfiguration createConfiguration() {
    CustomConfiguration config = CustomConfiguration.generateConfiguration(PersistenceModule.JPA_UNIT,
        BaseEntity.class.getPackage(), LocalDateConverter.class.getPackage());
    return config;
  }

  @Override
  public void execute(String... args) {
    CustomConfiguration config = createConfiguration();

    SchemaExport schemaExport = new SchemaExport(config);
    schemaExport.setOutputFile(DROP_CREATE_PATH);
    schemaExport.create(Target.SCRIPT);
  }
}
