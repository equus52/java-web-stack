package equus.webstack.command;

import org.slf4j.Logger;

public interface Command {

  void execute(String... args);

  Logger getLogger();

  default String getCommandName() {
    return this.getClass().getSimpleName();
  }

  default void executeCommand(String... args) {
    try {
      execute(args);
    } catch (Throwable t) {
      getLogger().error(String.format("%s is failed.", getCommandName()), t);
    }
    getLogger().info(String.format("%s is finished.", getCommandName()));
  }

}