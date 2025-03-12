package ma.sool.wiz;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class WizService {

  private final WizRepo wizRepo;

  public Wiz findById(Integer id) {
    return wizRepo.findById(id).orElseThrow(() -> new WizNotFoundException(id) );
  }

}
