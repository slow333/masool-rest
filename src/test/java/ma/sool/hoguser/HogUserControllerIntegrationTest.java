package ma.sool.hoguser;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.sool.system.StatusCode;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "dev")
public class HogUserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Value("${api.base-url}")
    String url;

    String token;

    @BeforeEach
    void setup() throws Exception {
        ResultActions resultActions = mockMvc.perform(post(url + "/users/login")
                .with(httpBasic("kim", "123456")));
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(contentAsString);
        this.token = "Bearer " + jsonObject.getJSONObject("data").getString("token");
    }

    @Test
    @DisplayName("FindAll (GET)")
    void testFindAllUsersSuccess() throws Exception {
        mockMvc.perform(get(url + "/users").accept(MediaType.APPLICATION_JSON).header("Authorization", token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)));
    }

    @Test
    @DisplayName("FindUsersById (GET)")
    void testFindByIdUsersSuccess() throws Exception {
        mockMvc.perform(get(url + "/users/2").accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Id Success"))
                .andExpect(jsonPath("$.data.id").value(2));
    }

    @Test
    @DisplayName("Check findUserById with non-existent id (GET)")
    void testFindUserByIdNotFound() throws Exception {
        this.mockMvc.perform(get(this.url + "/users/5").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Id 5"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    @DisplayName("Check addUser with valid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testAddUserSuccess() throws Exception {
        HogUser hogwartsUser = new HogUser();
        hogwartsUser.setUsername("lily");
        hogwartsUser.setPassword("123456");
        hogwartsUser.setEnabled(true);
        hogwartsUser.setRoles("admin user"); // The delimiter is space.

        String json = objectMapper.writeValueAsString(hogwartsUser);
        this.mockMvc.perform(post(this.url + "/users").accept(MediaType.APPLICATION_JSON)
                        .content(json).contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("lily"))
                .andExpect(jsonPath("$.data.roles").value("admin user"))
                .andExpect(jsonPath("$.data.enabled").value(true))     ;
        mockMvc.perform(get(url + "/users").accept(MediaType.APPLICATION_JSON).header("Authorization", token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(5)));
    }

    @Test
    @DisplayName("Check addUser with invalid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testAddUserErrorWithInvalidInput() throws Exception {
        HogUser hogwartsUser = new HogUser();
        hogwartsUser.setUsername("");
        hogwartsUser.setPassword("");
        hogwartsUser.setRoles("");

        String json = objectMapper.writeValueAsString(hogwartsUser);
        this.mockMvc.perform(post(this.url + "/users").accept(MediaType.APPLICATION_JSON)
                        .content(json).contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.username").value("username is required."))
                .andExpect(jsonPath("$.data.password").value("password is required."))
                .andExpect(jsonPath("$.data.roles").value("roles is required."));
        mockMvc.perform(get(url + "/users").accept(MediaType.APPLICATION_JSON).header("Authorization", token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)));
    }
    @Test
    @DisplayName("Check update with valid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testUpdateUserWithAdminSuccess() throws Exception {
        HogUser hogwartsUser = new HogUser();
        hogwartsUser.setUsername("woo-update");
        hogwartsUser.setEnabled(false);
        hogwartsUser.setRoles("user"); // The delimiter is space.

        String json = objectMapper.writeValueAsString(hogwartsUser);

        this.mockMvc.perform(put(this.url + "/users/2").accept(MediaType.APPLICATION_JSON)
                        .content(json).contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("woo-update"))
                .andExpect(jsonPath("$.data.roles").value("user"))
                .andExpect(jsonPath("$.data.enabled").value(false))     ;
        mockMvc.perform(get(url + "/users").accept(MediaType.APPLICATION_JSON).header("Authorization", token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)));
    }
    @Test
    @DisplayName("Check update with non existent user input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testUpdateUserWithNonExistentUser() throws Exception {
        HogUser hogwartsUser = new HogUser();
        hogwartsUser.setUsername("woo-update");
        hogwartsUser.setEnabled(false);
        hogwartsUser.setRoles("user"); // The delimiter is space.

        String json = objectMapper.writeValueAsString(hogwartsUser);

        this.mockMvc.perform(put(this.url + "/users/6").accept(MediaType.APPLICATION_JSON)
                        .content(json).contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Id 6"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    @DisplayName("Check deleteUser with valid input (DELETE)")
    void testDeleteUserSuccess() throws Exception {
        this.mockMvc.perform(delete(url + "/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
        this.mockMvc.perform(get(url + "/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Id 2"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    @DisplayName("Check deleteUser with non existent id input (DELETE)")
    void testDeleteUserWithNonExistentUser() throws Exception {
        this.mockMvc.perform(delete(url + "/users/5").accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Id 5"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
