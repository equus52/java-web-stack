package equus.webstack.application;

import javax.inject.Inject;

import lombok.val;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;

import equus.webstack.application.module.ApplicationModule;
import equus.webstack.converter.json.CustomObjectMapper;

public class WebStackApplication extends ResourceConfig {
  @Inject
  public WebStackApplication(ServiceLocator serviceLocator) {
    packages("equus.webstack.resource");
    property("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", false);
    property("com.sun.jersey.api.json.POJOMappingFeature", true);

    GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
    GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
    guiceBridge.bridgeGuiceInjector(createInjector());

    JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
    provider.setMapper(new CustomObjectMapper());
    register(provider);
  }

  public static Injector createInjector() {
    val injector = Guice.createInjector(new ServletModule() {
      @Override
      protected void configureServlets() {
        install(new ApplicationModule());
      }
    });
    val initializer = injector.getInstance(WebStackInitializer.class);
    initializer.start();
    return injector;
  }
}
