package ma.sool.wiz;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.sool.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WizService {

  private final WizRepo wizRepo;

  public Wiz findById(Integer id) {
    return wizRepo.findById(id).orElseThrow(() -> new ObjectNotFoundException("wiz", id) );
  }

  public List<Wiz> findAll(){
    return wizRepo.findAll();
  }

  public Wiz saveWiz(Wiz wiz) {
    Wiz savedWiz = wizRepo.save(wiz);
    return savedWiz;
  }

  public Wiz updateWiz(Integer id, Wiz wiz) {
    Wiz foundWiz = wizRepo.findById(id).orElseThrow(() -> new ObjectNotFoundException("wiz", id));
    foundWiz.setName(wiz.getName());
    Wiz savedWiz = wizRepo.save(foundWiz);
    return savedWiz;
  }

  public void deleteWiz(Integer id) {
    Wiz wiz = wizRepo.findById(id).orElseThrow(() -> new ObjectNotFoundException("wiz", id));
    wiz.removeAllArt();
    wizRepo.deleteById(id);
  }
}
