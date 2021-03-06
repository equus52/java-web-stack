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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import equus.webstack.application.module.ApplicationModule;
import equus.webstack.converter.json.CustomObjectMapper;

public class WebStackApplication extends ResourceConfig {

  private static Injector injector = null;

  public static Injector getInjector() {
    return injector;
  }

  // for test
  public static void setInjector(Injector injector) {
    WebStackApplication.injector = injector;
  }

  @Inject
  @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
  public WebStackApplication(ServiceLocator serviceLocator) {
    packages("equus.webstack.resource");
    property("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", false);
    property("com.sun.jersey.api.json.POJOMappingFeature", true);

    GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
    GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
    if (injector == null) {
      injector = createInjector();
    }
    guiceBridge.bridgeGuiceInjector(injector);

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
