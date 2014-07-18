package equus.webstack.persist.configuration;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Mappings;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.MappedSuperclass;
import org.hibernate.mapping.PersistentClass;

@SuppressWarnings("serial")
public class CustomConfiguration extends Configuration {

  @Override
  protected void secondPassCompile() throws MappingException {
    super.secondPassCompile();
    customCompile();
  }

  private void customCompile() {
    processTableComment();
    processColumnComment();
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
      Map<String, Comment> commentMapping = createCommentMapping(persistentClass);

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

  private Map<String, Comment> createCommentMapping(PersistentClass persistentClass) {
    Map<String, Comment> commentMapping = new HashMap<>();
    MappedSuperclass superMappedSuperclass = persistentClass.getSuperMappedSuperclass();
    if (superMappedSuperclass != null) {
      if (superMappedSuperclass.getSuperPersistentClass() != null) {
        commentMapping.putAll(createCommentMapping(superMappedSuperclass.getSuperPersistentClass()));
      } else {
        commentMapping.putAll(createCommentMapping(superMappedSuperclass.getMappedClass()));
      }
    }
    commentMapping.putAll(createCommentMapping(persistentClass.getMappedClass()));
    return commentMapping;
  }

  private Map<String, Comment> createCommentMapping(Class<?> mappedClass) {
    Map<String, Comment> commentMapping = new HashMap<>();
    for (Field field : mappedClass.getDeclaredFields()) {
      Comment comment = field.getAnnotation(Comment.class);
      if (comment == null) {
        continue;
      }
      String columnName = getColumnName(field);
      commentMapping.put(columnName, comment);
    }
    return commentMapping;
  }

  private String getColumnName(Field field) {
    Mappings mappings = createMappings();
    return mappings.getObjectNameNormalizer().normalizeIdentifierQuoting(
        mappings.getNamingStrategy().propertyToColumnName(field.getName()));
  }
}
