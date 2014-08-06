package equus.webstack.service;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import lombok.RequiredArgsConstructor;
import equus.webstack.model.Customer;

@RequiredArgsConstructor(onConstructor = @__({ @Inject }))
public class CustomerServiceImpl implements CustomerService {
  private final EntityManager em;

  @Override
  public EntityManager getEntityManager() {
    return em;
  }

  @Override
  public Class<Customer> getEntityClass() {
    return Customer.class;
  }
}
