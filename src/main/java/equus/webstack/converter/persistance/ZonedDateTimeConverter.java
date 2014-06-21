package equus.webstack.converter.persistance;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

  @Override
  public Timestamp convertToDatabaseColumn(ZonedDateTime dateTime) {
    return Timestamp.from(dateTime.toInstant());
  }

  @Override
  public ZonedDateTime convertToEntityAttribute(Timestamp timestamp) {
    return ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault());
  }

}
