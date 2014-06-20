package equus.webstack;

import org.glassfish.jersey.server.ResourceConfig;

public class WebStackApplication extends ResourceConfig {
  public WebStackApplication() {
    packages("equus.webstack.resource");
    property("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", false);
    property("com.sun.jersey.api.json.POJOMappingFeature", true);
  }
}
