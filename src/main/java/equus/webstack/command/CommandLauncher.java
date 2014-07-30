package equus.webstack.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

@Slf4j
public class CommandLauncher {
  public static void main(String... args) {
    CommandLauncher launcher = new CommandLauncher();
    Params params = new Params();
    CmdLineParser parser = new CmdLineParser(params);
    try {
      parser.parseArgument(args);
    } catch (CmdLineException e) {
      log.error("unexpected args.", e);
      log.info("command list: " + launcher.getCommandNameList());
      parser.printUsage(System.out);
      return;
    }
    try {
      launcher.execute(params);
    } catch (Throwable t) {
      log.error("command failed.", t);
      System.exit(-1);
    }
    System.exit(0);
  }

  @Data
  private static class Params {
    @Option(name = "-n", aliases = "--name", required = true, usage = "command name")
    private String name;

    @Argument(index = 0, metaVar = "arguments...", handler = StringArrayOptionHandler.class)
    private String[] arguments;
  }

  private Map<String, Command> commandMap;
  {
    List<Command> list = new ArrayList<>();
    list.add(new DBDropCreateCommand());
    list.add(new DBInitializeCommand());
    list.add(new GenerateDDLCommand());

    Map<String, Command> map = new LinkedHashMap<>();
    list.forEach(c -> map.put(c.getCommandName(), c));
    commandMap = Collections.unmodifiableMap(map);
  }

  private Set<String> getCommandNameList() {
    return commandMap.keySet();
  }

  public void execute(Params params) {
    String commandName = params.getName();
    Command command = commandMap.get(commandName);
    if (command == null) {
      throw new RuntimeException("unknown command name: " + commandName);
    }
    command.executeCommand(params.getArguments());
  }
}
