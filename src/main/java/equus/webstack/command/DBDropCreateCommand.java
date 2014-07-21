package equus.webstack.command;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.Target;
import org.slf4j.Logger;

import equus.webstack.model.BaseEntity;
import equus.webstack.persist.configuration.CustomConfiguration;
import equus.webstack.persist.module.PersistModule;

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

    CustomConfiguration config = CustomConfiguration.generateConfiguration(PersistModule.JPA_UNIT,
        BaseEntity.class.getPackage());

    SchemaExport schemaExport = new SchemaExport(config);
    schemaExport.setOutputFile(GenerateDDLCommand.DROP_CREATE_PATH);
    schemaExport.create(Target.BOTH);
  }
}
