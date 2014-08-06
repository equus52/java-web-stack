package equus.webstack.service.module;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import equus.webstack.service.PersistenceService;

public class PersistenceServiceInterceptorTest {

  @Test
  public void test_EXCLUDE_MTHODS() {
    Set<String> methodNames = Arrays.stream(PersistenceService.class.getMethods()).map(m -> m.getName())
        .collect(Collectors.toSet());
    for (String name : PersistenceServiceInterceptor.EXCLUDE_MTHODS) {
      if (!methodNames.contains(name)) {
        fail(String.format("Method not found. method name: [%s]", name));
      }
    }
  }
}
