package ma.sool.hoguser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.sool.system.StatusCode;
import ma.sool.system.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

  @MockBean
  UserService userService;

  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  MockMvc mockMvc;
  @Value("${api.base-url}")
          String baseUrl;

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
  void findAllUsersSuccess() throws Exception {
    given(userService.findAll()).willReturn(hogUsers);
    // when and then
    mockMvc.perform(get(baseUrl+"/users").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Find All Success"))
            .andExpect(jsonPath("$.data", Matchers.hasSize(4)));
  }

  @Test
  void findByIdSuccess() throws Exception {
    HogUser u4 = new HogUser();
    u4.setId(4L);
    u4.setUsername("dong");
    u4.setEnabled(false);
    u4.setRoles("user");
    given(userService.findById(4L)).willReturn(u4);
    // when and then
    mockMvc.perform(get(baseUrl+"/users/4").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Find Id Success"))
            .andExpect(jsonPath("$.data.id").value(4L))
            .andExpect(jsonPath("$.data.username").value("dong"))
            .andExpect(jsonPath("$.data.roles").value("user"))
    ;
  }
  @Test
  void findByIdNotFound() throws Exception {
    // given
    given(userService.findById(Mockito.anyLong()))
            .willThrow(new ObjectNotFoundException("user", 2));
    // when and then
    mockMvc.perform(get(baseUrl + "/users/2")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
            .andExpect(jsonPath("$.message").value("Could not find user with Id 2"))
            .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void addUserSuccess() throws Exception {
    HogUser newUser = new HogUser();
    newUser.setId(5L);
    newUser.setUsername("dong new");
    newUser.setPassword("123");
    newUser.setEnabled(false);
    newUser.setRoles("user");

    String json = objectMapper.writeValueAsString(newUser);
    given(userService.addUser(newUser)).willReturn(newUser);
    // when and then
    mockMvc.perform(post(baseUrl+"/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Add Success"))
            .andExpect(jsonPath("$.data.id").isNotEmpty())
            .andExpect(jsonPath("$.data.username").value("dong new"))
            .andExpect(jsonPath("$.data.roles").value("user"))
    ;
  }

  @Test
  void updateUserSuccess() throws Exception {
    // given
    UserDto userDto = new UserDto(3L, "tommy", false, "admin");

    HogUser updateUser = new HogUser();
    updateUser.setId(3L);
    updateUser.setUsername("tommy new");
    updateUser.setEnabled(false);
    updateUser.setRoles("user");
    String json = objectMapper.writeValueAsString(userDto);

    given(userService.update(eq(3L), Mockito.any(HogUser.class))).willReturn(updateUser);

    // When and Then
    mockMvc.perform(put(baseUrl+"/users/3")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Update Success"))
            .andExpect(jsonPath("$.data.id").isNotEmpty())
            .andExpect(jsonPath("$.data.username").value("tommy new"))
            .andExpect(jsonPath("$.data.roles").value("user"))
    ;
  }

  @Test
  void updateUserNotFound() throws Exception {
    // given
    UserDto userDto = new UserDto(3L, "tommy", false, "admin");
    String json = objectMapper.writeValueAsString(userDto);

    given(userService.update(eq(3L), Mockito.any(HogUser.class)))
            .willThrow(new ObjectNotFoundException("user", 3L));

    // When and Then
    mockMvc.perform(put(baseUrl+"/users/3")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
            .andExpect(jsonPath("$.message").value("Could not find user with Id 3"))
            .andExpect(jsonPath("$.data").isEmpty())
    ;
  }

  @Test
  void deleteUserSuccess() throws Exception {
    // given
    doNothing().when(userService).deleteById(3L);
    // When and Then
    mockMvc.perform(delete(baseUrl+"/users/3")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Delete Success"))
            .andExpect(jsonPath("$.data").isEmpty())
    ;
  }
  @Test
  void deleteUserNotFound() throws Exception {
    // given
    doThrow(new ObjectNotFoundException("user", 3L)).when(userService).deleteById(3L);
    // When and Then
    mockMvc.perform(delete(baseUrl+"/users/3")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
            .andExpect(jsonPath("$.message").value("Could not find user with Id 3"))
            .andExpect(jsonPath("$.data").isEmpty())
    ;
  }
}