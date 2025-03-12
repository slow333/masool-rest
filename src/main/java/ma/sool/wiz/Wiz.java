package ma.sool.wiz;

import jakarta.persistence.*;
import lombok.*;
import ma.sool.art.Art;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "wizard")
public class Wiz implements Serializable {

  @Id
  @GeneratedValue
  Integer id;

  String name;

  @OneToMany(mappedBy = "owner", cascade={CascadeType.PERSIST, CascadeType.MERGE})
  private List<Art> arts = new ArrayList<>();

  public void addArt(Art art) {
    art.setOwner(this);
    arts.add(art);
  }
  public Integer getNumberOfArts(){
    return arts.size();
  };
}
