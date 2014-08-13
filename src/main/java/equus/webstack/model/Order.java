package equus.webstack.model;

import java.time.ZonedDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "order_id")
  private Set<OrderItem> orderItemList;
}
