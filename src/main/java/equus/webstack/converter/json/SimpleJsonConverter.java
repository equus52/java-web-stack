package equus.webstack.converter.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;

public interface SimpleJsonConverter<T> extends JsonConverter<T> {

  String encode(T value);

  T decode(String jsonValue);

  @Override
  default void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
      JsonProcessingException {
    if (value == null) {
      return;
    }
    jgen.writeString(encode(value));
  }

  @SuppressWarnings("null")
  @Override
  default T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    String text = jp.getText();
    if (text == null) {
      return null;
    }
    return decode(text);
  }

}
