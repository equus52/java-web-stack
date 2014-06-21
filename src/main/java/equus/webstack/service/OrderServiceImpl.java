package equus.webstack.service;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import lombok.RequiredArgsConstructor;
import equus.webstack.model.Order;

@RequiredArgsConstructor(onConstructor = @__({ @Inject }))
public class OrderServiceImpl implements OrderService {

  private final EntityManager em;

  @Override
  public EntityManager getEntityManager() {
    return em;
  }

  @Override
  public Class<Order> getEntityClass() {
    return Order.class;
  }

}
