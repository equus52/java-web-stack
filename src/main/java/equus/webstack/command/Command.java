package equus.webstack.command;

import org.slf4j.Logger;

public interface Command {

  void execute(String... args);

  String getName();

  Logger getLogger();

  default void executeCommand(String... args) {
    try {
      execute(args);
    } catch (Throwable t) {
      getLogger().error(String.format("%s is failed.", getName()), t);
      System.exit(-1);
    }
    getLogger().info(String.format("%s is finished.", getName()));
    System.exit(0);
  }

}