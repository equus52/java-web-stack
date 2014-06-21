package equus.webstack.application;

import javax.inject.Inject;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;

import equus.webstack.application.module.ApplicationModule;

public class WebStackApplication extends ResourceConfig {
  @Inject
  public WebStackApplication(ServiceLocator serviceLocator) {
    packages("equus.webstack.resource");
    property("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", false);
    property("com.sun.jersey.api.json.POJOMappingFeature", true);

    GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
    GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
    guiceBridge.bridgeGuiceInjector(createInjector());
  }

  private Injector createInjector() {
    return Guice.createInjector(new ServletModule() {
      @Override
      protected void configureServlets() {
        install(new ApplicationModule());
      }
    });
  }
}
