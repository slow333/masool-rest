package ma.sool.wiz;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.sool.art.Art;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(value = "dev")
class WizControllerTest {

  @Autowired
  MockMvc mockMvc;
  @MockitoBean
  private WizService wizService;

  @Autowired
  ObjectMapper objectMapper;

  @Value("${api.base-url}")
  String baseUrl;

  List<Wiz> wizs;

  @BeforeEach
  void setUp() {
    Art a1 = new Art();
    a1.setId("1250808601744904191");
    a1.setName("Deluminator");
    a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter....");
    a1.setImgUrl("ImageUrl");

    Art a2 = new Art();
    a2.setId("1250808601744904192");
    a2.setName("Invisibility Cloak");
    a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
    a2.setImgUrl("ImageUrl");

    Art a3 = new Art();
    a3.setId("1250808601744904193");
    a3.setName("Elder Wand");
    a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, ...");
    a3.setImgUrl("ImageUrl");

    Art a4 = new Art();
    a4.setId("1250808601744904194");
    a4.setName("The Marauder's Map");
    a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, ....");
    a4.setImgUrl("ImageUrl");

    Art a5 = new Art();
    a5.setId("1250808601744904195");
    a5.setName("The Sword Of Gryffindor");
    a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by ....");
    a5.setImgUrl("ImageUrl");

    Art a6 = new Art();
    a6.setId("1250808601744904196");
    a6.setName("Resurrection Stone");
    a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, ....");
    a6.setImgUrl("ImageUrl");

    this.wizs = new ArrayList<>();

    Wiz w1 = new Wiz();
    w1.setId(1);
    w1.setName("Albus Dumbledore");
    w1.addArt(a1);
    w1.addArt(a3);
    this.wizs.add(w1);

    Wiz w2 = new Wiz();
    w2.setId(2);
    w2.setName("Harry Potter");
    w2.addArt(a2);
    w2.addArt(a4);
    this.wizs.add(w2);

    Wiz w3 = new Wiz();
    w3.setId(3);
    w3.setName("Neville Longbottom");
    w3.addArt(a5);
    this.wizs.add(w3);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void findWizByIdSuccess() throws Exception {
    // given
    given(wizService.findById(3)).willReturn(wizs.get(2));
    // when and then
    mockMvc.perform(get(baseUrl+"/wizs/3").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Find One Success"))
            .andExpect(jsonPath("$.data.id").value(3))
            .andExpect(jsonPath("$.data.name").value("Neville Longbottom"));

  }
  
  @Test
  void findWizByIdNotFound() throws Exception {
    // given
      given(wizService.findById(Mockito.anyInt())).willThrow(new ObjectNotFoundException("wiz", 2));
    // when and then
    mockMvc.perform(get(baseUrl + "/wizs/2")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
            .andExpect(jsonPath("$.message").value("Could not find wiz with Id 2"))
            .andExpect(jsonPath("$.data").isEmpty());
  }
  @Test
  void FindAllSuccess() throws Exception {
    // Given
    given(wizService.findAll()).willReturn(wizs);
    // When and Then
    mockMvc.perform(get(baseUrl + "/wizs").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Find all Success"))
            .andExpect(jsonPath("$.data", Matchers.hasSize(wizs.size())));
  }
  @Test
  void testAddWizSuccess() throws Exception {
    // Given
    WizDto dto = new WizDto(33, "Neville ADD", null);
    String json = objectMapper.writeValueAsString(dto);

    Wiz w3 = new Wiz();
    w3.setId(33);
    w3.setName("Neville ADD");

    given(wizService.saveWiz(w3)).willReturn(w3);
    // When and Then
    mockMvc.perform(post(baseUrl + "/wizs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Add Success"))
            .andExpect(jsonPath("$.data.id").value(33))
            .andExpect(jsonPath("$.data.name").value("Neville ADD"))
    ;
  }
  @Test
  void testUpdateSuccess() throws Exception {
    // Given
    WizDto oldDto = new WizDto(1, "Albus Dumbledore", null);
    String json = objectMapper.writeValueAsString(oldDto);

    Wiz update = new Wiz();
    update.setId(1);
    update.setName("Albus UPDATE");
    given(wizService.updateWiz(eq(1), Mockito.any(Wiz.class))).willReturn(update);

    // When and Then
    mockMvc.perform(put(baseUrl + "/wizs/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Update Success"))
            .andExpect(jsonPath("$.data.id").value(update.getId()))
            .andExpect(jsonPath("$.data.name").value(update.getName()))
    ;  }

  @Test
  void testUpdateNotFound() throws Exception {
    // Given
    WizDto oldDto = new WizDto(1, "Albus Dumbledore", null);
    String json = objectMapper.writeValueAsString(oldDto);

    given(wizService.updateWiz(eq(1), Mockito.any(Wiz.class))).willThrow(new ObjectNotFoundException("wiz", 1));

    // When and Then
    mockMvc.perform(put(baseUrl + "/wizs/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
            .andExpect(jsonPath("$.message").value("Could not find wiz with Id 1"))
            .andExpect(jsonPath("$.data").isEmpty())
    ;  }
  @Test
  void testDeleteSuccess() throws Exception {
    // given
    doNothing().when(this.wizService).deleteWiz(2);

    // When and Then
    mockMvc.perform(delete(baseUrl+"/wizs/2")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Delete Success"))
            .andExpect(jsonPath("$.data").isEmpty());
  }
  @Test
  void testDeleteErrorWithId() throws Exception {
    // given
    doThrow(new ObjectNotFoundException("wiz", 2)).when(this.wizService).deleteWiz(2);

    // When and Then
    mockMvc.perform(delete(baseUrl+"/wizs/2")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
            .andExpect(jsonPath("$.message").value("Could not find wiz with Id 2"))
            .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void testChangeArtOwnerSuccess() throws Exception {
    // Given
    doNothing().when(wizService).changeArtOwner(2, "1250808601744904196");

    // When and Then
    mockMvc.perform(put(baseUrl+"/wizs/2/arts/1250808601744904196")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Art Change owner Success"))
            .andExpect(jsonPath("$.data").isEmpty());
  }
  @Test
  void testChangeArtOwnerWithNoWizId() throws Exception {
    // Given
    doThrow(new ObjectNotFoundException("wiz", 5))
            .when(wizService).changeArtOwner(5, "1250808601744904196");

    // When and Then
    mockMvc.perform(put(baseUrl+"/wizs/5/arts/1250808601744904196")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
            .andExpect(jsonPath("$.message").value("Could not find wiz with Id 5"))
            .andExpect(jsonPath("$.data").isEmpty());
  }
  @Test
  void testChangeArtOwnerWithNoArtId() throws Exception {
    // Given
    doThrow(new ObjectNotFoundException("art", "1250808601744904199"))
            .when(wizService).changeArtOwner(5, "1250808601744904199");

    // When and Then
    mockMvc.perform(put(baseUrl+"/wizs/5/arts/1250808601744904199")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
            .andExpect(jsonPath("$.message").value("Could not find art with Id 1250808601744904199"))
            .andExpect(jsonPath("$.data").isEmpty());
  }

}