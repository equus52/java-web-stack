package equus.webstack.application;

import javax.inject.Inject;

import lombok.RequiredArgsConstructor;

import com.google.inject.persist.PersistService;

@RequiredArgsConstructor(onConstructor = @__({ @Inject }))
public class WebStackFinalizer {
  private final PersistService persistService;

  public void stop() {
    persistService.stop();
  }
}
