package equus.webstack.converter.json;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeJsonConverter implements SimpleJsonConverter<ZonedDateTime> {

  @Override
  public String encode(ZonedDateTime value) {
    return value.format(DateTimeFormatter.ISO_DATE);
  }

  @Override
  public ZonedDateTime decode(String jsonValue) {
    return ZonedDateTime.parse(jsonValue);
  }

}
