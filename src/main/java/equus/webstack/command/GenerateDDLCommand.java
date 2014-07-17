package equus.webstack.command;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.hibernate.cfg.Configuration;
import org.hibernate.jpa.boot.internal.ParsedPersistenceXmlDescriptor;
import org.hibernate.jpa.boot.internal.PersistenceXmlParser;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.Target;

import equus.webstack.model.BaseEntity;
import equus.webstack.model.Customer;
import equus.webstack.model.Order;
import equus.webstack.model.OrderItem;
import equus.webstack.service.module.ServiceModule;

public class GenerateDDLCommand {

  public static void main(String[] args) {
    new GenerateDDLCommand().execute();
  }

  public void execute() {
    Properties properties = null;
    @SuppressWarnings("rawtypes")
    List<ParsedPersistenceXmlDescriptor> units = PersistenceXmlParser.locatePersistenceUnits(new HashMap());
    for (ParsedPersistenceXmlDescriptor persistenceUnit : units) {
      if (persistenceUnit.getName().equals(ServiceModule.JPA_UNIT)) {
        properties = persistenceUnit.getProperties();
      }
    }
    if (properties == null) {
      throw new RuntimeException("properties not found.");
    }
    Configuration config = new Configuration();
    config.addProperties(properties);
    // TODO:java-web-stack class scan
    config.addAnnotatedClass(BaseEntity.class);
    config.addAnnotatedClass(Customer.class);
    config.addAnnotatedClass(Order.class);
    config.addAnnotatedClass(OrderItem.class);

    SchemaExport schemaExport = new SchemaExport(config);
    schemaExport.setOutputFile("ddl/drop_create.sql");
    schemaExport.create(Target.SCRIPT);
  }
}
