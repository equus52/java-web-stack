package equus.webstack.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
public class Student extends BaseEntity {

  @Column(nullable = false)
  private String name;

  @Column(nullable = true)
  @ManyToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SUBSELECT)
  private Set<Course> courseList;
}
