package equus.webstack.command;

public class DBInitializeWithTestDataCommand {

  public static void main(String[] args) {
    new DBInitializeWithTestDataCommand().execute();
  }

  public void execute() {
    new DBInitializeCommand().execute(injector -> {
      // setup test data
      });
  }

}
