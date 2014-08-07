package equus.webstack.command;

import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;

import com.google.inject.Injector;

import equus.webstack.application.WebStackApplication;
import equus.webstack.application.WebStackFinalizer;

@Slf4j
public class DBInitializeCommand implements Command {

  @Override
  public Logger getLogger() {
    return log;
  }

  public static void main(String[] args) {
    new DBInitializeCommand().executeCommand();
  }

  @Override
  public void execute(String... args) {
    execute(injector -> {
    });
  }

  public void execute(Consumer<Injector> block) {
    new DBDropCreateCommand().execute();
    Injector injector = null;
    try {
      injector = WebStackApplication.createInjector();
      // setup initial data

      block.accept(injector);
    } finally {
      if (injector != null) {
        injector.getInstance(WebStackFinalizer.class).stop();
      }
    }
  }

}
