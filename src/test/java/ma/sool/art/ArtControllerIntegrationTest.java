package ma.sool.art;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.sool.system.StatusCode;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration Test Artifact, API endpoint")
@Tag("통합시험")
@ActiveProfiles(value = "dev")
public class ArtControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Value("${api.base-url}")
    String url;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    @BeforeEach
    void setup() throws Exception {
        // jwt 생성을 위한 방법
        // pom.xml에 spring-security-test 추가 필요
        ResultActions result = mockMvc.perform(post(url + "/users/login")
                .with(httpBasic("kim", "123456")));
        MvcResult mvcResult = result.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(contentAsString);
        this.token = "Bearer " + jsonObject.getJSONObject("data").getString("token");
    }
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("arts find all 통합시험")
    void testFindAllSuccess() throws Exception {
        mockMvc.perform(get(url + "/arts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(6)));
    }
    @Test
    @DisplayName("포스트에 대한 Add arts 시험")
    void testSaveArtSuccess() throws Exception {
        // given
        Art a = new Art();
        a.setName("new arts");
        a.setDescription("add artifact tech");
        a.setImgUrl("image-url");

        String json = objectMapper.writeValueAsString(a);
        // when and then
        mockMvc.perform(post(url+"/arts")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("new arts"));
        mockMvc.perform(get(url+"/arts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(7)));
    }
    @Test
    @DisplayName("arts By id")
    void testFindByIdSuccess() throws Exception {
        mockMvc.perform(get(url + "/arts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("Deluminator"));
    }

    @Test
    @DisplayName("arts By id not found")
    void testFindByIdNotFound() throws Exception {
        mockMvc.perform(get(url + "/arts/1250808601744904199").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find art with Id 1250808601744904199"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("arts By id not found")
    void testDeleteSuccess() throws Exception {
        mockMvc.perform(delete(url + "/arts/1250808601744904192")
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("arts update success")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testArtUpdateSuccess() throws Exception {
        ArtDto art = new ArtDto("1250808601744904192", "update artifact","새로 정의한 마술 기술입니다.", "imageurl", null);

        String json = objectMapper.writeValueAsString(art);

        mockMvc.perform(put(url + "/arts/1250808601744904192")
                        .header("Authorization", token)
                        .content(json).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.name").value("update artifact"))
                .andExpect(jsonPath("$.data.description").value("새로 정의한 마술 기술입니다."));
    }
}
