package equus.webstack.application;

import javax.inject.Inject;

import lombok.RequiredArgsConstructor;

import com.google.inject.persist.PersistService;

@RequiredArgsConstructor(onConstructor = @__({ @Inject }))
public class WebStackInitializer {
  private final PersistService persistService;

  public void start() {
    persistService.start();
  }
}
