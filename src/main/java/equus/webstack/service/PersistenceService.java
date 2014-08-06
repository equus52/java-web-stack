package equus.webstack.service;

import java.util.List;

import javax.persistence.EntityManager;

import lombok.val;

import com.google.inject.persist.Transactional;

import equus.webstack.model.BaseEntity;

public interface PersistenceService<T extends BaseEntity> {

  EntityManager getEntityManager();

  Class<T> getEntityClass();

  default List<T> findAll() {
    Class<T> entityClass = getEntityClass();
    val builder = getEntityManager().getCriteriaBuilder();
    val criteria = builder.createQuery(entityClass);
    criteria.from(entityClass);
    return getEntityManager().createQuery(criteria).getResultList();
  }

  default T findByPrimaryKey(long id) {
    return getEntityManager().find(getEntityClass(), id);
  }

  default T findByVersion(long id, long version) {
    Class<T> entityClass = getEntityClass();
    val builder = getEntityManager().getCriteriaBuilder();
    val criteria = builder.createQuery(entityClass);
    val from = criteria.from(entityClass);

    val idPredicate = builder.equal(from.get("id"), id);
    val versionPredicate = builder.equal(from.get("version"), version);
    criteria.where(builder.and(idPredicate, versionPredicate));
    return getEntityManager().createQuery(criteria).getSingleResult();
  }

  @Transactional
  default T save(T entity) {
    getEntityManager().persist(entity);
    return entity;
  }

  @Transactional
  default T update(T entity) {
    getEntityManager().merge(entity);
    return entity;
  }

  @Transactional
  default void delete(T entity) {
    getEntityManager().remove(entity);
  }
}
