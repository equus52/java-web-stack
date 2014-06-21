package equus.webstack.converter.json;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateJsonConverter implements SimpleJsonConverter<LocalDate> {

  @Override
  public String encode(LocalDate value) {
    return value.format(DateTimeFormatter.ISO_DATE);
  }

  @Override
  public LocalDate decode(String jsonValue) {
    return LocalDate.parse(jsonValue);
  }

}
