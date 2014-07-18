package equus.webstack.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import equus.webstack.persist.configuration.ColumnOrder;
import equus.webstack.persist.configuration.Comment;

@MappedSuperclass
@Data
@SuppressWarnings("serial")
public abstract class BaseEntity implements Serializable {

  @Id
  @Comment("identifier")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Version
  private long version;

  @Column(nullable = false, updatable = false)
  @ColumnOrder(100)
  @JsonIgnore
  private ZonedDateTime createDateTime;

  @Column(nullable = false)
  @ColumnOrder(100)
  @JsonIgnore
  private ZonedDateTime updateDateTime;

  @PrePersist
  protected void updateCreateTimestamp() {
    this.createDateTime = ZonedDateTime.now();
    this.updateDateTime = ZonedDateTime.now();
  }

  @PreUpdate
  public void updateUpdateTimestamp() {
    this.updateDateTime = ZonedDateTime.now();
  }
}
