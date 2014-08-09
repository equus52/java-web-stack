package equus.webstack.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
public class Student extends BaseEntity {

  @Column(nullable = false)
  private String name;

  @Column(nullable = true)
  @ManyToMany(fetch = FetchType.EAGER)
  private List<Course> courseList;
}