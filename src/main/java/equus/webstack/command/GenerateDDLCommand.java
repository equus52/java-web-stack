package equus.webstack.command;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.Target;
import org.slf4j.Logger;

import equus.webstack.persist.configuration.CustomConfiguration;

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

  @Override
  public void execute(String... args) {
    CustomConfiguration config = CustomConfiguration.generateConfiguration();

    SchemaExport schemaExport = new SchemaExport(config);
    schemaExport.setOutputFile(DROP_CREATE_PATH);
    schemaExport.create(Target.SCRIPT);
  }
}
