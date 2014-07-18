package equus.webstack.command;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.persistence.Entity;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.cfg.Configuration;
import org.hibernate.jpa.boot.internal.ParsedPersistenceXmlDescriptor;
import org.hibernate.jpa.boot.internal.PersistenceXmlParser;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.Target;
import org.reflections.Reflections;
import org.slf4j.Logger;

import equus.webstack.model.BaseEntity;
import equus.webstack.persist.configuration.CustomConfiguration;
import equus.webstack.persist.module.PersistModule;

@Slf4j
public class GenerateDDLCommand implements Command {

  @Override
  public Logger getLogger() {
    return log;
  }

  @Override
  public String getName() {
    return this.getClass().getName();
  }

  public static void main(String[] args) {
    new GenerateDDLCommand().execute();
  }

  @Override
  public void execute(String... args) {
    Properties properties = null;
    @SuppressWarnings("rawtypes")
    List<ParsedPersistenceXmlDescriptor> units = PersistenceXmlParser.locatePersistenceUnits(new HashMap());
    for (ParsedPersistenceXmlDescriptor persistenceUnit : units) {
      if (persistenceUnit.getName().equals(PersistModule.JPA_UNIT)) {
        properties = persistenceUnit.getProperties();
      }
    }
    if (properties == null) {
      throw new RuntimeException("properties not found.");
    }
    Configuration config = new CustomConfiguration();
    config.addProperties(properties);
    getAnnotatedClassed().forEach(c -> config.addAnnotatedClass(c));

    SchemaExport schemaExport = new SchemaExport(config);
    schemaExport.setOutputFile("ddl/drop_create.sql");
    schemaExport.create(Target.SCRIPT);
  }

  private Collection<Class<?>> getAnnotatedClassed() {
    val reflections = new Reflections(BaseEntity.class.getPackage().getName());
    return reflections.getTypesAnnotatedWith(Entity.class);
  }
}
