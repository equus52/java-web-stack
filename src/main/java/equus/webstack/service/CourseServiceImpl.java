package equus.webstack.service;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import lombok.RequiredArgsConstructor;
import equus.webstack.model.Course;

@RequiredArgsConstructor(onConstructor = @__({ @Inject }))
public class CourseServiceImpl implements CourseService {
  private final EntityManager em;

  @Override
  public EntityManager getEntityManager() {
    return em;
  }

  @Override
  public Class<Course> getEntityClass() {
    return Course.class;
  }
}
