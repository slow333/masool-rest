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
  @GeneratedValue(strategy = GenerationType.AUTO)
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

  public void removeAllArts() {
    arts.forEach(art -> art.setOwner(null));
    arts = null;
  }

  public void removeArt(Art selectedArt) {
    selectedArt.setOwner(null);
    arts.remove(this);
  }
}
