package equus.webstack.service.module;

import javax.persistence.EntityManager;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import equus.webstack.service.PersistenceService;

public class PersistenceServiceInterceptor implements MethodInterceptor {
  @SuppressWarnings("rawtypes")
  public static final Class<PersistenceService> TARGET_CLASS = PersistenceService.class;
  static String[] EXCLUDE_MTHODS = new String[] { "getEntityManager", "getEntityClass" };

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Object ret = null;
    try {
      ret = invocation.proceed();
    } finally {
      if (isTarget(invocation)) {
        postInvoke(invocation, ret);
      }
    }
    return ret;
  }

  protected void postInvoke(MethodInvocation invocation, Object ret) {
    if (ret == null) {
      return;
    }
    PersistenceService<?> service = (PersistenceService<?>) invocation.getThis();
    EntityManager em = service.getEntityManager();
    if (!em.isJoinedToTransaction()) {
      detach(em, ret);
    }
  }

  private void detach(EntityManager em, Object value) {
    if (value instanceof Iterable) {
      for (Object element : (Iterable<?>) value) {
        detach(em, element);
      }
    } else {
      em.detach(value);
    }
  }

  private boolean isTarget(MethodInvocation invocation) {
    for (String excludeMethod : EXCLUDE_MTHODS) {
      if (invocation.getMethod().getName().equals(excludeMethod)) {
        return false;
      }
    }
    return true;
  }
}