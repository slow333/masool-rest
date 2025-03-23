package ma.sool.wiz;

import com.fasterxml.jackson.core.JsonProcessingException;
import ma.sool.art.Art;
import ma.sool.art.ArtRepo;
import ma.sool.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "dev")
class WizServiceTest {

  @Mock
  WizRepo wizRepo;
  @Mock
  ArtRepo artRepo;
  @InjectMocks
  WizService wizService;

  List<Wiz> wizs=new ArrayList<>();

  @BeforeEach
  void setUp() {
    Wiz w1 = new Wiz();
    w1.setId(1);
    w1.setName("Albus Dumbledore");
    this.wizs.add(w1);

    Wiz w2 = new Wiz();
    w2.setId(2);
    w2.setName("Harry Potter");
    this.wizs.add(w2);

    Wiz w3 = new Wiz();
    w3.setId(3);
    w3.setName("Neville Longbottom");
    this.wizs.add(w3);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void findWizByIdSuccess() {
    // Given
    Wiz w = new Wiz();
    w.setId(1);
    w.setName("Albus Dumbledore");
    given(wizRepo.findById(1)).willReturn(Optional.of(w));
    // when
    Wiz result = wizService.findById(1);
    // then
    assertThat(result.getId()).isEqualTo(w.getId());
    assertThat(result.getName()).isEqualTo(w.getName());
    verify(wizRepo, times(1)).findById(1);
  }
  @Test
  void findWizByIdNotFound() {
    given(wizRepo.findById(1)).willReturn(Optional.empty());
    // when
    Throwable thrown = catchThrowable(() -> {
      Wiz result = wizService.findById(1);
    });
    // then
    assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
    verify(wizRepo, times(1)).findById(1);
  }
  @Test
  void findAllSuccess() {
    // Given
    given(wizRepo.findAll()).willReturn(wizs);
    // when
    List<Wiz> allWiz = wizService.findAll();
    // then
    assertThat(allWiz.size()).isEqualTo(3);
  }
  @Test
  void testSaveWizSuccess() {
    // Given
    Wiz wiz = new Wiz();
    wiz.setId(33);
    wiz.setName("Albus new");

    given(wizRepo.save(wiz)).willReturn(wiz);
    // When
    Wiz savedWiz = wizService.saveWiz(wiz);
    // then
    assertThat(savedWiz.getId()).isEqualTo(33);
    assertThat(savedWiz.getName()).isEqualTo("Albus new");
  }
  @Test
  void testUpdateWizSuccess(){
    // Given
    Wiz old = new Wiz();
    old.setName("Albus Dumbledore");
    old.setId(1);
    Wiz newWiz = new Wiz();
    newWiz.setId(1);
    newWiz.setName("update wiz");

    given(wizRepo.findById(1)).willReturn(Optional.of(old));
    given(wizRepo.save(old)).willReturn(old);

    // When
    Wiz updateWiz = wizService.updateWiz(1, newWiz);
    // Then
    assertThat(updateWiz.getName()).isEqualTo("update wiz");
  }
  @Test
  void testUpdateWizNotFound(){
    // Given
    Wiz old = new Wiz();
    old.setName("Albus Dumbledore");
    old.setId(1);

    given(wizRepo.findById(1)).willReturn(Optional.empty());

    // When
    assertThrows(ObjectNotFoundException.class, () -> {
      wizService.updateWiz(1, old);
    });

    // Then
    verify(wizRepo, times(1)).findById(1);
  }
  @Test
  void testDeleteSuccess() {
    // Given
    Wiz wiz = new Wiz();
    wiz.setName("Albus Dumbledore");
    wiz.setId(1);
    given(wizRepo.findById(1)).willReturn(Optional.of(wiz));
    doNothing().when(wizRepo).deleteById(1);
    // When
    wizService.deleteWiz(1);
    // Then
    verify(wizRepo, times(1)).deleteById(1);
  }
  @Test
  void testDeleteErrorWithId() {
    // Given
    given(wizRepo.findById(2)).willReturn(Optional.empty());
    // When
    assertThrows(ObjectNotFoundException.class, () -> {
      wizService.deleteWiz(2);
    });
    // Then
    verify(wizRepo, times(1)).findById(2);
  }
  @Test
  void testChangeArtOwnerSuccess() throws JsonProcessingException {
    // Given
    Art assignArt = new Art();
    assignArt.setId("1250808601744904192");
    assignArt.setName("Invisibility Cloak");
    assignArt.setDescription("An invisibility cloak is used to make the wearer invisible.");
    assignArt.setImgUrl("ImageUrl");

    Wiz oldWiz = new Wiz();
    oldWiz.setName("Albus Dumbledore");
    oldWiz.setId(1);
    oldWiz.addArt(assignArt);

    Wiz newWiz = new Wiz();
    newWiz.setId(2);
    newWiz.setName("Harry Potter");

    given(wizRepo.findById(2)).willReturn(Optional.of(newWiz));
    given(artRepo.findById("1250808601744904192")).willReturn(Optional.of(assignArt));

    // When
    wizService.changeArtOwner(2, "1250808601744904192");
    // Then
    assertThat(assignArt.getOwner().getId()).isEqualTo(2);
    assertThat(newWiz.getArts()).contains(assignArt);
  }

  @Test
  void testChangeArtOwnerNoWizId() throws JsonProcessingException {
    // Given
    Art assignArt = new Art();
    assignArt.setId("1250808601744904192");
    assignArt.setName("Invisibility Cloak");
    assignArt.setDescription("An invisibility cloak is used to make the wearer invisible.");
    assignArt.setImgUrl("ImageUrl");

    Wiz oldWiz = new Wiz();
    oldWiz.setName("Albus Dumbledore");
    oldWiz.setId(1);
    oldWiz.addArt(assignArt);

    given(artRepo.findById("1250808601744904192")).willReturn(Optional.of(assignArt));
    given(wizRepo.findById(2)).willReturn(Optional.empty());

    // When
    Throwable thrown = assertThrows(ObjectNotFoundException.class, () ->{
      wizService.changeArtOwner(2, "1250808601744904192");
    });
    // Then
    assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
            .hasMessage("Could not find wiz with Id 2");
    assertThat(assignArt.getOwner().getId()).isEqualTo(1);
  }

  @Test
  void testChangeArtOwnerWithNoArtId() throws JsonProcessingException {
    // Given
    given(artRepo.findById("1250808601744904192")).willReturn(Optional.empty());

    // When
    Throwable thrown = assertThrows(ObjectNotFoundException.class, () ->{
      wizService.changeArtOwner(2, "1250808601744904192");
    });
    // Then
    assertThat(thrown)
            .isInstanceOf(ObjectNotFoundException.class)
            .hasMessage("Could not find art with Id 1250808601744904192");
  }
}