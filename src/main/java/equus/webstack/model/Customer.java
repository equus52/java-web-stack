package equus.webstack.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
public class Customer extends BaseEntity {

  @Column(nullable = false)
  private String name;
}
