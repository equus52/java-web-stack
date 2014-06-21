package equus.webstack.converter.json;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@SuppressWarnings("serial")
public class CustomObjectMapper extends ObjectMapper {

  public CustomObjectMapper() {
    super();
    CustomModule module = new CustomModule();
    module.addConverter(LocalDate.class, new LocalDateJsonConverter());
    module.addConverter(ZonedDateTime.class, new ZonedDateTimeJsonConverter());
    registerModule(module);
  }

  public static class CustomModule extends SimpleModule {

    public CustomModule() {
      super();
    }

    public <T> void addConverter(Class<T> clazz, JsonConverter<T> converter) {
      addDeserializer(clazz, converter.getDeserializer());
      addSerializer(clazz, converter.getSerializer());
    }
  }
}
