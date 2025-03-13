package ma.sool.art;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ArtControllerTest {

  @MockBean
  ArtService artService;

  @Value("${api.base-url}")
  String baseUrl;

  @Autowired
  MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;

  List<Art> arts;

  @BeforeEach
  void setUp() {
    this.arts = new ArrayList<>();
    Art a1 = new Art();
    a1.setId("1250808601744904191");
    a1.setName("Deluminator");
    a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
    a1.setImgUrl("ImageUrl");
    this.arts.add(a1);

    Art a2 = new Art();
    a2.setId("1250808601744904192");
    a2.setName("Invisibility Cloak");
    a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
    a2.setImgUrl("ImageUrl");
    this.arts.add(a2);

    Art a3 = new Art();
    a3.setId("1250808601744904193");
    a3.setName("Elder Wand");
    a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
    a3.setImgUrl("ImageUrl");
    this.arts.add(a3);

    Art a4 = new Art();
    a4.setId("1250808601744904194");
    a4.setName("The Marauder's Map");
    a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
    a4.setImgUrl("ImageUrl");
    this.arts.add(a4);

    Art a5 = new Art();
    a5.setId("1250808601744904195");
    a5.setName("The Sword Of Gryffindor");
    a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
    a5.setImgUrl("ImageUrl");
    this.arts.add(a5);

    Art a6 = new Art();
    a6.setId("1250808601744904196");
    a6.setName("Resurrection Stone");
    a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
    a6.setImgUrl("ImageUrl");
    this.arts.add(a6);
  }

  @AfterEach
  void tearDown() {}

  @Test
  void testGetArtByIdSuccess() throws Exception {
    //given
    given(artService.findById("1250808601744904196")).willReturn(arts.get(5));
    //when and then
    mockMvc.perform(get("/api/v1/arts/1250808601744904196")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Find One Success"))
            .andExpect(jsonPath("$.data.id").value("1250808601744904196"))
            .andExpect(jsonPath("$.data.name").value("Resurrection Stone"));
  }
  @Test
  void testGetArtByIdNotFound() throws Exception {
    //given
    given(artService.findById("1250808601744904196")).willThrow(new ObjectNotFoundException("art", "1250808601744904196"));
    //when and then
    mockMvc.perform(get(baseUrl+"/arts/1250808601744904196")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
            .andExpect(jsonPath("$.message").value("Could not find art with Id 1250808601744904196"))
            .andExpect(jsonPath("$.data").isEmpty());
  }
  @Test
  void testGetArtAllSuccess() throws Exception {
    //given
    given(artService.findAll()).willReturn(arts);
    //when and then
    mockMvc.perform(get(baseUrl+"/arts").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Find All"))
            .andExpect(jsonPath("$.data", Matchers.hasSize(arts.size())))
            .andExpect(jsonPath("$.data[0].id").value("1250808601744904191"))
            .andExpect(jsonPath("$.data[0].name").value("Deluminator"));
  }

  @Test
  void testCreateArtSuccess() throws Exception {
    //given
    ArtDto artDto = new ArtDto(null, "added Masool", "추가 마술", "imageUrl", null);
    String json = objectMapper.writeValueAsString(artDto);
    Art a = new Art();
    a.setId("1250808601744904191");
    a.setName("added Masool");
    a.setDescription("추가 마술");
    a.setImgUrl("ImageUrl");

    given(artService.createArt(Mockito.any(Art.class))).willReturn(a);
    //when and then
    mockMvc.perform(post(baseUrl+"/arts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json).accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Add Success"))
            .andExpect(jsonPath("$.data.id").value(a.getId()))
            .andExpect(jsonPath("$.data.name").value(a.getName()))
            .andExpect(jsonPath("$.data.description").value(a.getDescription()));
  }
  @Test
  void testUpdateArtSuccess() throws Exception {
    ArtDto artDto = new ArtDto("1250808601744904191", "Invisibility Cloak", "An invisibility cloak is used to make the wearer invisible.", "imageUrl", null);

    String json = objectMapper.writeValueAsString(artDto);
    Art art = new Art();
    art.setId("1250808601744904191");
    art.setName("added Masool");
    art.setDescription("추가 마술");
    art.setImgUrl("ImageUrl");

    given(artService.updateArt(eq("1250808601744904191"), Mockito.any(Art.class))).willReturn(art);
    //when and then
    mockMvc.perform(put(baseUrl+"/arts/1250808601744904191")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json).accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Update Success"))
            .andExpect(jsonPath("$.data.id").value(art.getId()))
            .andExpect(jsonPath("$.data.name").value(art.getName()))
            .andExpect(jsonPath("$.data.description").value(art.getDescription()))
            .andExpect(jsonPath("$.data.imgUrl").value(art.getImgUrl()));
  }
  @Test
  void testUpdatErrorNoId() throws Exception {
    ArtDto artDto = new ArtDto("1250808601744904192", "Invisibility Cloak", "An invisibility cloak is used to make the wearer invisible.", "imageUrl", null);

    String json = objectMapper.writeValueAsString(artDto);
    given(artService.updateArt(eq("1250808601744904192"), Mockito.any(Art.class)))
            .willThrow(new ObjectNotFoundException("art","1250808601744904192"));
    //when and then
    mockMvc.perform(put(baseUrl+"/arts/1250808601744904192")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json).accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
            .andExpect(jsonPath("$.message").value("Could not find art with Id 1250808601744904192"))
            .andExpect(jsonPath("$.data").isEmpty());
  }
  @Test
  void testDeleteSuccess() throws Exception {
    // Given
    doNothing().when(this.artService).deleteArt("1250808601744904191");

    // When and Then
    mockMvc.perform(delete(baseUrl+"/arts/1250808601744904191")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Delete Success"))
            .andExpect(jsonPath("$.data").isEmpty());
  }
  @Test
  void testDeleteNotFound() throws Exception {
    // Given
    doThrow(new ObjectNotFoundException("art", "1250808601744904191"))
            .when(this.artService).deleteArt("1250808601744904191");

    // When and Then
    mockMvc.perform(delete(baseUrl+"/arts/1250808601744904191")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
            .andExpect(jsonPath("$.message").value("Could not find art with Id 1250808601744904191"))
            .andExpect(jsonPath("$.data").isEmpty());
  }
}