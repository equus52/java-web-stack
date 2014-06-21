package equus.webstack.model;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "order_list")
@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
public class Order extends BaseEntity {

  @Column(nullable = false)
  private ZonedDateTime orderDateTime;

  @Column(nullable = true)
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "order_id")
  private List<OrderItem> orderItemList;
}
