package equus.webstack.converter.persistance;

import static com.google.common.base.CaseFormat.*;
import lombok.val;

import org.hibernate.cfg.EJB3NamingStrategy;

@SuppressWarnings("serial")
public class CustomNamingStrategy extends EJB3NamingStrategy {

  @Override
  public String classToTableName(String className) {
    val tableName = super.classToTableName(className);
    return UPPER_CAMEL.converterTo(LOWER_UNDERSCORE).convert(tableName);
  }

  @Override
  public String propertyToColumnName(String propertyName) {
    val columnName = super.propertyToColumnName(propertyName);
    return LOWER_CAMEL.converterTo(LOWER_UNDERSCORE).convert(columnName);
  }
}
