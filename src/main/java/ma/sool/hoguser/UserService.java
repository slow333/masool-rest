package ma.sool.hoguser;

import jakarta.validation.Valid;
import ma.sool.system.exception.ObjectNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

  private final UserRepo userRepo;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
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
    newHogUser.setPassword(passwordEncoder.encode(newHogUser.getPassword()));
    return userRepo.save(newHogUser);
  }

  public void deleteById(Long userId) {
    userRepo.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
    userRepo.deleteById(userId);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepo.findByUsername(username)
            .map(MyUserPrincipal::new)
            .orElseThrow(() -> new UsernameNotFoundException("username "+username +" 없어요."));
  }
}










