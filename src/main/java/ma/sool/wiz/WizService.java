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
}
