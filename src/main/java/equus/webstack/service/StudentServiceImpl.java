package equus.webstack.service;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import lombok.RequiredArgsConstructor;

import com.google.inject.persist.Transactional;

import equus.webstack.model.Student;

@RequiredArgsConstructor(onConstructor = @__({ @Inject }))
public class StudentServiceImpl implements StudentService {
  private final EntityManager em;

  @Override
  public EntityManager getEntityManager() {
    return em;
  }

  @Override
  public Class<Student> getEntityClass() {
    return Student.class;
  }

  @Override
  @Transactional
  public Student save(Student entity) {
    // List<Course> list = new ArrayList<>();
    // List<Course> prevList = entity.getCourseList();
    // if (prevList != null) {
    // for (val relation : prevList) {
    // list.add(em.merge(relation));
    // }
    // }
    // entity.setCourseList(list);
    // val merged = em.merge(entity);
    // em.persist(merged);
    em.persist(entity);
    return entity;
  }
}
