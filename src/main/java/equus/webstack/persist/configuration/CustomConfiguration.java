package equus.webstack.persist.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.val;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Mappings;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.jpa.AvailableSettings;
import org.hibernate.jpa.boot.internal.ParsedPersistenceXmlDescriptor;
import org.hibernate.jpa.boot.internal.PersistenceXmlParser;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.MappedSuperclass;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;
import org.reflections.Reflections;

import equus.webstack.model.BaseEntity;
import equus.webstack.persist.module.PersistModule;

@SuppressWarnings("serial")
public class CustomConfiguration extends Configuration {

  @SneakyThrows
  public static CustomConfiguration generateConfiguration() {
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
    CustomConfiguration config = new CustomConfiguration();
    config.addProperties(properties);
    config.namingStrategy = (NamingStrategy) ReflectHelper.classForName(
        properties.getProperty(AvailableSettings.NAMING_STRATEGY)).newInstance();
    getAnnotatedClassed().forEach(c -> config.addAnnotatedClass(c));

    return config;
  }

  private static Collection<Class<?>> getAnnotatedClassed() {
    val reflections = new Reflections(BaseEntity.class.getPackage().getName());
    return reflections.getTypesAnnotatedWith(Entity.class);
  }

  @Setter
  private Comparator<OrderingColumn> columnComparator = OrderingColumn.defaultComparator;

  @Override
  protected void secondPassCompile() throws MappingException {
    super.secondPassCompile();
    customCompile();
  }

  private void customCompile() {
    processTableComment();
    processColumnComment();
    processColumnOrder();
  }

  private void processTableComment() {
    for (PersistentClass persistentClass : classes.values()) {
      Class<?> mappedClass = persistentClass.getMappedClass();
      Comment comment = mappedClass.getAnnotation(Comment.class);
      if (comment == null) {
        continue;
      }
      persistentClass.getTable().setComment(comment.value());
    }
  }

  private void processColumnComment() {
    for (PersistentClass persistentClass : classes.values()) {
      Map<String, Comment> commentMapping = createFieldMapping(persistentClass,
          field -> field.getAnnotation(Comment.class));

      @SuppressWarnings("rawtypes")
      Iterator columnIterator = persistentClass.getTable().getColumnIterator();
      while (columnIterator.hasNext()) {
        Column column = (Column) columnIterator.next();
        Comment comment = commentMapping.get(column.getName());
        if (comment != null) {
          column.setComment(comment.value());
        }
      }
    }
  }

  private <T> LinkedHashMap<String, T> createFieldMapping(PersistentClass persistentClass, Function<Field, T> function) {
    LinkedHashMap<String, T> commentMapping = new LinkedHashMap<>();
    MappedSuperclass superMappedSuperclass = persistentClass.getSuperMappedSuperclass();
    if (superMappedSuperclass != null) {
      if (superMappedSuperclass.getSuperPersistentClass() != null) {
        commentMapping.putAll(createFieldMapping(superMappedSuperclass.getSuperPersistentClass(), function));
      } else {
        commentMapping.putAll(createFieldMapping(superMappedSuperclass.getMappedClass(), function));
      }
    }
    commentMapping.putAll(createFieldMapping(persistentClass.getMappedClass(), function));
    return commentMapping;
  }

  private <T> LinkedHashMap<String, T> createFieldMapping(Class<?> mappedClass, Function<Field, T> function) {
    LinkedHashMap<String, T> commentMapping = new LinkedHashMap<>();
    for (Field field : mappedClass.getDeclaredFields()) {
      String columnName = getColumnName(field);
      commentMapping.put(columnName, function.apply(field));
    }
    return commentMapping;
  }

  private String getColumnName(Field field) {
    Mappings mappings = createMappings();
    return mappings.getObjectNameNormalizer().normalizeIdentifierQuoting(
        mappings.getNamingStrategy().propertyToColumnName(field.getName()));
  }

  private void processColumnOrder() {
    for (PersistentClass persistentClass : classes.values()) {
      LinkedHashMap<String, ColumnOrder> orderMapping = createFieldMapping(persistentClass,
          field -> field.getAnnotation(ColumnOrder.class));
      List<String> idColmun = findAnnotatedFields(persistentClass, Id.class);
      List<String> versionColmun = findAnnotatedFields(persistentClass, Version.class);

      List<OrderingColumn> orderingColumns = new ArrayList<>();
      @SuppressWarnings("rawtypes")
      Iterator columnIterator = persistentClass.getTable().getColumnIterator();
      while (columnIterator.hasNext()) {
        Column column = (Column) columnIterator.next();
        Integer fieldIndex = indexOf(orderMapping, column.getName());
        ColumnOrder order = orderMapping.get(column.getName());
        int columnOrder = order != null ? order.value() : 0;
        boolean id = idColmun.contains(column.getName());
        boolean version = versionColmun.contains(column.getName());
        orderingColumns.add(new OrderingColumn(fieldIndex, columnOrder, column, id, version));
      }
      updateColumns(persistentClass.getTable(), orderingColumns);
    }
  }

  private <T extends Annotation> List<String> findAnnotatedFields(PersistentClass persistentClass, Class<T> annotation) {
    LinkedHashMap<String, T> mapping = createFieldMapping(persistentClass, field -> field.getAnnotation(annotation));
    List<String> ret = new ArrayList<>();
    for (val entry : mapping.entrySet()) {
      if (entry.getValue() == null) {
        continue;
      }
      ret.add(entry.getKey());
    }
    return ret;
  }

  private Integer indexOf(LinkedHashMap<String, ColumnOrder> orderMapping, String name) {
    Integer index = null;
    int counter = 0;
    for (String columnName : orderMapping.keySet()) {
      if (columnName.equals(name)) {
        index = counter;
        break;
      }
      counter++;
    }
    return index;
  }

  @SneakyThrows
  private void updateColumns(Table table, List<OrderingColumn> orderingColumns) {
    orderingColumns.sort(columnComparator);

    Class<? extends Table> tableClass = table.getClass();
    Field f = tableClass.getDeclaredField("columns");
    f.setAccessible(true);
    @SuppressWarnings("rawtypes")
    Map columns = (Map) f.get(table);
    columns.clear();

    for (OrderingColumn orderingColumn : orderingColumns) {
      table.addColumn(orderingColumn.column);
    }
  }

  @Value
  private static class OrderingColumn {
    Integer fieldIndex;
    int columnOrder;
    Column column;
    boolean id;
    boolean version;

    static final Comparator<OrderingColumn> defaultComparator = (o1, o2) -> {
      CompareToBuilder builder = new CompareToBuilder();
      builder.append(o2.id, o1.id);
      builder.append(o2.version, o1.version);
      builder.append(o1.columnOrder, o2.columnOrder);
      builder.append(o1.fieldIndex, o2.fieldIndex);
      return builder.toComparison();
    };
  }
}
