package ma.sool.wiz;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.sool.art.Art;
import ma.sool.art.ArtRepo;
import ma.sool.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WizService {

  private final WizRepo wizRepo;
  private final ArtRepo artRepo;

  public Wiz findById(Integer id) {
    return wizRepo.findById(id).orElseThrow(() -> new ObjectNotFoundException("wiz", id) );
  }

  public List<Wiz> findAll(){
    return wizRepo.findAll();
  }

  public Wiz saveWiz(Wiz wiz) {
    return wizRepo.save(wiz);
  }

  public Wiz updateWiz(Integer id, Wiz wiz) {
    return wizRepo.findById(id).map(oldWiz -> {
              oldWiz.setName(wiz.getName());
              return wizRepo.save(oldWiz); })
            .orElseThrow(() -> new ObjectNotFoundException("wiz", id));
  }

  public void deleteWiz(Integer id) {
    Wiz wizDeleted = wizRepo.findById(id).orElseThrow(() -> new ObjectNotFoundException("wiz", id));
    wizDeleted.removeAllArts();
    wizRepo.deleteById(id);
  }

  public void changeArtOwner(Integer wizId, String artId) {
    Art foundArt = artRepo.findById(artId)
            .orElseThrow(() -> new ObjectNotFoundException("art", artId));
    Wiz foundWiz = wizRepo.findById(wizId)
            .orElseThrow(() -> new ObjectNotFoundException("wiz", wizId));
    if (foundArt.getOwner() != null) {
      foundArt.getOwner().removeArt(foundArt);
    }
    foundWiz.addArt(foundArt);
  }
}
