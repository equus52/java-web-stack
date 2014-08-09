package equus.webstack.application.module;

import lombok.val;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;

import equus.webstack.service.module.ServiceMockModule;

public final class TestModules {

  public static Injector createWebMockInjector() {
    val injector = Guice.createInjector(new ServletModule() {
      @Override
      protected void configureServlets() {
        install(new ServiceMockModule());
      }
    });
    return injector;
  }
}
