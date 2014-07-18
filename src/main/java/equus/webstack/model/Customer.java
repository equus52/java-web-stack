package equus.webstack.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import equus.webstack.persist.configuration.Comment;

@Entity
@Comment("Customer")
@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
public class Customer extends BaseEntity {

  @Column(nullable = false)
  @Comment("Customer Name")
  private String name;
}
