package equus.webstack.converter.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

@SuppressWarnings("serial")
public class CustomPrettyPrinter extends DefaultPrettyPrinter {

  public CustomPrettyPrinter(int arrayLinefeedLimitLevel, int objectLinefeedLimitLevel) {
    super();
    _arrayIndenter = new CustomeLf2SpacesIndenter(arrayLinefeedLimitLevel);
    _objectIndenter = new CustomeLf2SpacesIndenter(objectLinefeedLimitLevel);
  }

  private static class CustomeLf2SpacesIndenter extends Lf2SpacesIndenter {
    private final int linefeedLimitLevel;

    public CustomeLf2SpacesIndenter(int linefeedLimitLevel) {
      super();
      this.linefeedLimitLevel = linefeedLimitLevel;
    }

    @Override
    public void writeIndentation(JsonGenerator jg, int level) throws IOException {
      if (level > linefeedLimitLevel) {
        jg.writeRaw(' ');
        return;
      }
      super.writeIndentation(jg, level);
    }
  }
}