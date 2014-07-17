package equus.webstack.command;

import java.util.function.Consumer;

import lombok.val;

import com.google.inject.Injector;

import equus.webstack.application.WebStackApplication;
import equus.webstack.application.WebStackFinalizer;

public class DBInitializeCommand {

  public static void main(String[] args) {
    new DBInitializeCommand().execute();
  }

  public void execute() {
    execute(injector -> {
    });
  }

  public void execute(Consumer<Injector> block) {
    new DBDropCreateCommand().execute();
    val injector = WebStackApplication.createInjector();
    // setup initial data

    block.accept(injector);
    injector.getInstance(WebStackFinalizer.class).stop();
  }

}
