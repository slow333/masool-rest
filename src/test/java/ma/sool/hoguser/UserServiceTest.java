package ma.sool.hoguser;

import ma.sool.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  UserRepo userRepo;
  @Mock
  PasswordEncoder passwordEncoder;

  @InjectMocks
  UserService userService;

  List<HogUser> hogUsers;

  @BeforeEach
  void setUp() {
    HogUser u1 = new HogUser();
    u1.setId(1L);
    u1.setUsername("kim");
    u1.setEnabled(true);
    u1.setRoles("admin user");

    HogUser u2 = new HogUser();
    u2.setId(2L);
    u2.setUsername("woo");
    u2.setEnabled(true);
    u2.setRoles("user");

    HogUser u3 = new HogUser();
    u3.setId(3L);
    u3.setUsername("jin");
    u3.setEnabled(true);
    u3.setRoles("user");

    HogUser u4 = new HogUser();
    u4.setId(4L);
    u4.setUsername("dong");
    u4.setEnabled(false);
    u4.setRoles("user");
    
    hogUsers = new ArrayList<>();
    hogUsers.add(u1);
    hogUsers.add(u2);
    hogUsers.add(u3);
    hogUsers.add(u4);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void testFindAllSuccess() {
    // given
    given(userRepo.findAll()).willReturn(hogUsers);
    // when
    List<HogUser> allUsers = userService.findAll();
    // then
    assertThat(allUsers.size()).isEqualTo(4);
    verify(userRepo, times(1)).findAll();
  }

  @Test
  void findByIdSuccess() {
    // Given
    HogUser u1 = new HogUser();
    u1.setId(1L);
    u1.setUsername("kim");
    u1.setEnabled(true);
    u1.setRoles("admin user");

    given(userRepo.findById(1L)).willReturn(Optional.of(u1));
    // When
    HogUser foundUser = userService.findById(1L);
    // Then
    assertThat(foundUser.getUsername()).isEqualTo("kim");
    assertThat(foundUser.getId()).isEqualTo(1L);
    assertThat(foundUser.getRoles()).isEqualTo("admin user");
  }
  @Test
  void findByIdNotFound() {
    // Given
    given(userRepo.findById(1L)).willReturn(Optional.empty());
    // When
    Throwable thrown = catchThrowable(() -> {
      HogUser foundUser = userService.findById(1L);
    });
    // Then
    assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
            .hasMessage("Could not find user with Id 1");
  }

  @Test
  void updateSuccess() {
    // given
    HogUser oldUser = new HogUser();
    oldUser.setId(1L);
    oldUser.setUsername("kim");
    oldUser.setEnabled(true);
    oldUser.setRoles("admin user");

    HogUser updateUser = new HogUser();
    updateUser.setUsername("kim update");
    updateUser.setEnabled(true);
    updateUser.setRoles("admin user root");
    given(userRepo.findById(1L)).willReturn(Optional.of(oldUser));
    given(userRepo.save(oldUser)).willReturn(oldUser);
    // When
    HogUser update = userService.update(1L, updateUser);
    // then
    assertThat(update.getUsername()).isEqualTo("kim update");
    assertThat(update.getRoles()).isEqualTo("admin user root");
  }
  @Test
  void updateNotFound() {
    // given
    HogUser oldUser = new HogUser();
    oldUser.setId(1L);
    oldUser.setUsername("kim");
    oldUser.setEnabled(true);
    oldUser.setRoles("admin user");

    given(userRepo.findById(1L)).willReturn(Optional.empty());
    // When
    Throwable thrown = catchThrowable(() ->{
      HogUser update = userService.update(1L, oldUser);
    });
    // then
    assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
            .hasMessage("Could not find user with Id 1");
  }

  @Test
  void addSuccess() {
    HogUser u1 = new HogUser();
    u1.setId(1L);
    u1.setUsername("kim");
    u1.setPassword("123456");
    u1.setEnabled(true);
    u1.setRoles("admin user");

    given(passwordEncoder.encode(u1.getPassword())).willReturn("");
    given(userRepo.save(u1)).willReturn(u1);
    // When
    HogUser foundUser = userService.addUser(u1);
    // Then
    assertThat(foundUser.getUsername()).isEqualTo("kim");
    assertThat(foundUser.getId()).isEqualTo(1L);
    assertThat(foundUser.getRoles()).isEqualTo("admin user");
  }

  @Test
  void deleteByIdSuccess() {
    // Given
    HogUser u1 = new HogUser();
    u1.setId(1L);
    u1.setUsername("kim");
    u1.setEnabled(true);
    u1.setRoles("admin user");

    given(userRepo.findById(1L)).willReturn(Optional.of(u1));
    doNothing().when(userRepo).deleteById(1L);
    // When
    userService.deleteById(1L);
    // Then
  }
  @Test
  void deleteByIdNotFound() {
    // Given
    given(userRepo.findById(2L)).willReturn(Optional.empty());
    // When
    assertThrows(ObjectNotFoundException.class, () -> {
      userService.deleteById(2L);
    });
    // Then
    verify(userRepo, times(1)).findById(2L);
  }
}