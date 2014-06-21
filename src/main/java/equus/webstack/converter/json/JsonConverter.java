package equus.webstack.converter.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public interface JsonConverter<T> {

  void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException;

  T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException;

  default JsonSerializer<T> getSerializer() {
    return new JsonSerializer<T>() {
      @Override
      public void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
          JsonProcessingException {
        JsonConverter.this.serialize(value, jgen, provider);
      }
    };
  }

  default JsonDeserializer<T> getDeserializer() {
    return new JsonDeserializer<T>() {
      @Override
      public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return JsonConverter.this.deserialize(jp, ctxt);
      }
    };
  }
}
