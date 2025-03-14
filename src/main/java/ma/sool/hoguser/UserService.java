package ma.sool.hoguser;

import jakarta.validation.Valid;
import ma.sool.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

  private final UserRepo userRepo;

  public UserService(UserRepo userRepo) {
    this.userRepo = userRepo;
  }


  public List<HogUser> findAll() {
    return userRepo.findAll();
  }

  public HogUser findById(Long userId) {
    return userRepo.findById(userId)
            .orElseThrow(() -> new ObjectNotFoundException("user", userId));
  }

  public HogUser update(Long id, HogUser hogUser) {
    HogUser oldUser = userRepo.findById(id).orElseThrow(() -> new ObjectNotFoundException("user", id));
    oldUser.setUsername(hogUser.getUsername());
    oldUser.setRoles(hogUser.getRoles());
    oldUser.setEnabled(hogUser.isEnabled());
    return userRepo.save(oldUser);
  }

  public HogUser addUser(@Valid HogUser newHogUser) {
    return userRepo.save(newHogUser);
  }

  public void deleteById(Long userId) {
    userRepo.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
    userRepo.deleteById(userId);
  }
}
