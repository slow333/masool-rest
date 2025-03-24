package ma.sool.wiz;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration Test Wizards, API endpoint")
@Tag("통합시험")
@ActiveProfiles(value = "dev")
public class WizControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    WizService wizService;
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
    void testWizFindAllSuccess() throws Exception {
        mockMvc.perform(get(url+"/wizs").accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testWizFindByIdSuccess() throws Exception {
        mockMvc.perform(get(url+"/wizs/2").accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("Harry Potter"));
    }

    @Test
    void testWizFindByIdNotFound() throws Exception {
        mockMvc.perform(get(url+"/wizs/6").accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wiz with Id 6"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testWizSaveSuccess() throws Exception {

        Wiz newWiz = new Wiz();
//        newWiz.setId(4);
        newWiz.setName("new Wizard");
//        newWiz.setArts(null);

        String json = objectMapper.writeValueAsString(newWiz);

        mockMvc.perform(post(url+"/wizs")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization", token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("new Wizard"));
    }

    @Test
    void testWizDeleteSuccess() throws Exception {
        mockMvc.perform(delete(url+"/wizs/2").accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testChangeArtOwnerSuccess() throws Exception {

        WizDto wizDto = new WizDto(2, "update wiz", null);
        Wiz wiz = wizService.findById(2);
        wizService.updateWiz(2, wiz);

        String json = objectMapper.writeValueAsString(wizDto);

        // When and Then
        mockMvc.perform(put(url+"/wizs/2/arts/1250808601744904196")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization", token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Art Change owner Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testUpdateWizSuccess() throws Exception {

        WizDto wizDto = new WizDto(2, "update wiz", null);

        String json = objectMapper.writeValueAsString(wizDto);

        // When and Then
        mockMvc.perform(put(url+"/wizs/2")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .header("Authorization", token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.name").value("update wiz"));
    }
}
