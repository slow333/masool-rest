package ma.sool.wiz;

import ma.sool.art.Art;
import ma.sool.art.ArtNotFoundException;
import ma.sool.system.StatusCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class WizControllerTest {

  @Autowired
  MockMvc mockMvc;
  @MockBean
  WizService wizService;

  List<Wiz> wizs;

  @BeforeEach
  void setUp() {
    Art a1 = new Art();
    a1.setId("1250808601744904191");
    a1.setName("Deluminator");
    a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
    a1.setImgUrl("ImageUrl");

    Art a2 = new Art();
    a2.setId("1250808601744904192");
    a2.setName("Invisibility Cloak");
    a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
    a2.setImgUrl("ImageUrl");

    Art a3 = new Art();
    a3.setId("1250808601744904193");
    a3.setName("Elder Wand");
    a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
    a3.setImgUrl("ImageUrl");

    Art a4 = new Art();
    a4.setId("1250808601744904194");
    a4.setName("The Marauder's Map");
    a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
    a4.setImgUrl("ImageUrl");

    Art a5 = new Art();
    a5.setId("1250808601744904195");
    a5.setName("The Sword Of Gryffindor");
    a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
    a5.setImgUrl("ImageUrl");

    Art a6 = new Art();
    a6.setId("1250808601744904196");
    a6.setName("Resurrection Stone");
    a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
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
    mockMvc.perform(get("/api/v1/wizs/2").accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Find One Success"))
            .andExpect(jsonPath("$.data.id").value(3))
            .andExpect(jsonPath("$.data.name").value("Neville Longbottom"));

  }
  
  @Test
  void findWizByIdNotFound() throws Exception {
    // given
      given(wizService.findById(Mockito.anyInt())).willThrow(new WizNotFoundException(2));
    // when and then
    mockMvc.perform(get("/api/v1/wizs/2")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
            .andExpect(jsonPath("$.message").value("Could not find wiz with Id 2"))
            .andExpect(jsonPath("$.data").isEmpty());
  }
}