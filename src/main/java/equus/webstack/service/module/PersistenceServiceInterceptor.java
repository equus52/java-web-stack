package equus.webstack.service.module;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import equus.webstack.service.PersistenceService;

public class PersistenceServiceInterceptor implements MethodInterceptor {
  @SuppressWarnings("rawtypes")
  public static Class<PersistenceService> TARGET_CLASS = PersistenceService.class;
  public static String[] EXCLUDE_MTHODS = new String[] { "getEntityManager", "getEntityClass" };

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Object ret = invocation.proceed();
    if (isTarget(invocation)) {
      PersistenceService<?> service = (PersistenceService<?>) invocation.getThis();
      service.getEntityManager().clear();
    }
    return ret;
  }

  public boolean isTarget(MethodInvocation invocation) {
    for (String excludeMethod : EXCLUDE_MTHODS) {
      if (invocation.getMethod().getName().equals(excludeMethod)) {
        return false;
      }
    }
    return true;
  }
}