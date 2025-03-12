package ma.sool.art;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import ma.sool.wiz.Wiz;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name="artifact")
public class Art implements Serializable {

  @Id
  private String id;
  private String name;
  private String description;
  private String imgUrl;

  @ManyToOne
  private Wiz owner;
}
