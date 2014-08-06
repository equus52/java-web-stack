package equus.webstack.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import equus.webstack.model.BaseEntity;
import equus.webstack.service.PersistenceService;

public interface EntityResource<T extends BaseEntity> {
  PersistenceService<T> getPersistenceService();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  default List<T> findAll() {
    return getPersistenceService().findAll();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  default T findByPrimaryKey(@PathParam("id") int id) {
    return getPersistenceService().findByPrimaryKey(id);
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  default T save(T entity) {
    return getPersistenceService().save(entity);
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  default T update(T entity) {
    return getPersistenceService().update(entity);
  }

  @DELETE
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  default void delete(@PathParam("id") int id, @QueryParam("version") int version) {
    getPersistenceService().deleteByVersion(id, version);
  }
}
