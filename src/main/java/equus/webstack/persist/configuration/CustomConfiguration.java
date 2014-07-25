package equus.webstack.persist.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
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

import com.google.common.collect.LinkedHashMultimap;

@SuppressWarnings("serial")
public class CustomConfiguration extends Configuration {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @SneakyThrows
  public static CustomConfiguration generateConfiguration(String jpaUnit, Package... entityPackages) {
    ParsedPersistenceXmlDescriptor unit = null;
    List<ParsedPersistenceXmlDescriptor> units = PersistenceXmlParser.locatePersistenceUnits(new HashMap());
    for (ParsedPersistenceXmlDescriptor persistenceUnit : units) {
      if (persistenceUnit.getName().equals(jpaUnit)) {
        unit = persistenceUnit;
      }
    }
    if (unit == null) {
      throw new RuntimeException("properties not found.");
    }
    CustomConfiguration config = new CustomConfiguration();
    config.addProperties(unit.getProperties());
    for (String className : unit.getManagedClassNames()) {
      Class<?> managedClass = ReflectHelper.classForName(className);
      if (managedClass.getAnnotation(Entity.class) != null) {
        config.addAnnotatedClass(managedClass);
      }
      if (managedClass.getAnnotation(Converter.class) != null) {
        config.addAttributeConverter((Class<? extends AttributeConverter>) managedClass);
      }
    }
    for (val addAnnotatedClass : getAnnotatedClasses(Entity.class, entityPackages)) {
      config.addAnnotatedClass(addAnnotatedClass);
    }
    for (val addAnnotatedClass : getAnnotatedClasses(Converter.class, entityPackages)) {
      config.addAttributeConverter((Class<? extends AttributeConverter>) addAnnotatedClass);
    }

    config.namingStrategy = (NamingStrategy) ReflectHelper.classForName(
        unit.getProperties().getProperty(AvailableSettings.NAMING_STRATEGY)).newInstance();
    return config;
  }

  private static Collection<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotation,
      Package... entityPackages) {
    Set<Class<?>> ret = new HashSet<>();
    for (Package entityPackage : entityPackages) {
      val reflections = new Reflections(entityPackage.getName());
      ret.addAll(reflections.getTypesAnnotatedWith(annotation));
    }
    return ret;
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
      val fieldMapping = createFieldMapping(persistentClass);

      @SuppressWarnings("rawtypes")
      Iterator columnIterator = persistentClass.getTable().getColumnIterator();
      while (columnIterator.hasNext()) {
        Column column = (Column) columnIterator.next();
        Optional<Field> field = Optional.ofNullable(getField(fieldMapping, column.getName()));
        Optional<Comment> comment = field.map(f -> f.getAnnotation(Comment.class));
        comment.ifPresent(c -> column.setComment(c.value()));
      }
    }
  }

  private String getColumnName(Field field) {
    Mappings mappings = createMappings();
    return mappings.getObjectNameNormalizer().normalizeIdentifierQuoting(
        mappings.getNamingStrategy().propertyToColumnName(field.getName()));
  }

  private void processColumnOrder() {
    LinkedHashMultimap<Table, PersistentClass> multiMap = LinkedHashMultimap.create();
    for (PersistentClass persistentClass : classes.values()) {
      multiMap.put(persistentClass.getTable(), persistentClass);
    }
    for (val entry : multiMap.asMap().entrySet()) {
      LinkedHashMap<Class<?>, LinkedHashMap<String, Field>> fieldMapping = new LinkedHashMap<>();
      for (val persistentClass : entry.getValue()) {
        fieldMapping.putAll(createFieldMapping(persistentClass));
      }
      Table table = entry.getKey();
      List<OrderingColumn> orderingColumns = new ArrayList<>();
      @SuppressWarnings("unchecked")
      Iterator<Column> columnIterator = table.getColumnIterator();
      while (columnIterator.hasNext()) {
        Column column = columnIterator.next();
        Integer fieldIndex = indexOf(fieldMapping, column.getName());
        Optional<Field> field = Optional.ofNullable(getField(fieldMapping, column.getName()));
        Optional<ColumnOrder> order = field.map(f -> f.getAnnotation(ColumnOrder.class));
        int columnOrder = order.isPresent() ? order.get().value() : 0;
        boolean id = field.isPresent() && field.get().getAnnotation(Id.class) != null;
        boolean version = field.isPresent() && field.get().getAnnotation(Version.class) != null;
        orderingColumns.add(new OrderingColumn(fieldIndex, columnOrder, column, id, version));
      }
      updateColumns(table, orderingColumns);
    }
  }

  private LinkedHashMap<Class<?>, LinkedHashMap<String, Field>> createFieldMapping(PersistentClass persistentClass) {
    LinkedHashMap<Class<?>, LinkedHashMap<String, Field>> fieldMapping = new LinkedHashMap<>();
    if (persistentClass.getSuperclass() != null) {
      fieldMapping.putAll(createFieldMapping(persistentClass.getSuperclass()));
    }
    if (persistentClass.getSuperMappedSuperclass() != null) {
      fieldMapping.putAll(createFieldMapping(persistentClass.getSuperMappedSuperclass()));
    }
    Class<?> mappedClass = persistentClass.getMappedClass();
    fieldMapping.put(mappedClass, createFieldMapping(mappedClass));
    return fieldMapping;
  }

  private LinkedHashMap<Class<?>, LinkedHashMap<String, Field>> createFieldMapping(MappedSuperclass mappedSuperclass) {
    LinkedHashMap<Class<?>, LinkedHashMap<String, Field>> fieldMapping = new LinkedHashMap<>();
    if (mappedSuperclass.getSuperPersistentClass() != null) {
      fieldMapping.putAll(createFieldMapping(mappedSuperclass.getSuperPersistentClass()));
    }
    if (mappedSuperclass.getSuperMappedSuperclass() != null) {
      fieldMapping.putAll(createFieldMapping(mappedSuperclass.getSuperMappedSuperclass()));
    }
    Class<?> mappedClass = mappedSuperclass.getMappedClass();
    fieldMapping.put(mappedClass, createFieldMapping(mappedClass));
    return fieldMapping;
  }

  private LinkedHashMap<String, Field> createFieldMapping(Class<?> mappedClass) {
    LinkedHashMap<String, Field> commentMapping = new LinkedHashMap<>();
    for (Field field : mappedClass.getDeclaredFields()) {
      commentMapping.put(getColumnName(field), field);
    }
    return commentMapping;
  }

  private Integer indexOf(LinkedHashMap<Class<?>, LinkedHashMap<String, Field>> fieldMapping, String name) {
    Integer index = null;
    int counter = 0;
    for (val map : fieldMapping.values()) {
      for (val columnName : map.keySet()) {
        if (columnName.equals(name)) {
          index = counter;
          break;
        }
        counter++;
      }
    }
    return index;
  }

  private Field getField(LinkedHashMap<Class<?>, LinkedHashMap<String, Field>> fieldMapping, String name) {
    for (val map : fieldMapping.values()) {
      for (val entry : map.entrySet()) {
        if (entry.getKey().equals(name)) {
          return entry.getValue();
        }
      }
    }
    return null;
  }

  @SneakyThrows
  private void updateColumns(Table table, List<OrderingColumn> orderingColumns) {
    Collections.sort(orderingColumns, columnComparator);

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
  public static class OrderingColumn {
    Integer fieldIndex;
    int columnOrder;
    Column column;
    boolean id;
    boolean version;

    public static final Comparator<OrderingColumn> defaultComparator = new Comparator<OrderingColumn>() {

      @Override
      public int compare(OrderingColumn o1, OrderingColumn o2) {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(o2.id, o1.id);
        builder.append(o2.version, o1.version);
        builder.append(o1.columnOrder, o2.columnOrder);
        builder.append(o1.fieldIndex, o2.fieldIndex);
        return builder.toComparison();
      }
    };
  }
}