package ma.sool.art;

import ma.sool.system.IdWorker;
import ma.sool.system.exception.ObjectNotFoundException;
import ma.sool.wiz.Wiz;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "dev")
class ArtServiceTest {

  @Mock
  ArtRepo artRepo;
  @Mock
  IdWorker idWorker;

  @InjectMocks
  ArtService artService;

  List<Art> arts = new ArrayList<>();

  @BeforeEach
  void setup(){
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
  }
  @AfterEach
  void tearDown(){  }

  @Test
  void testFindByIdSuccess() {
    // given. artRepo의 Mock obj를 생성. 값입력
    Art a = new Art();
    a.setId("1250808601744904192");
    a.setName("Invisibility Cloak");
    a.setDescription("An invisibility cloak is used ...");
    a.setImgUrl("ImageUrl");
    Wiz w = new Wiz();
    w.setId(2);
    w.setName("Harry Potter");

    a.setOwner(w);

    given(artRepo.findById("1250808601744904192")).willReturn(Optional.of(a));
    // when 시험할 메서드에 대한 정의
    Art artResult = artService.findById("1250808601744904192");
    // then Assert 기대되는 결과
    assertThat(artResult.getId()).isEqualTo(a.getId());
    assertThat(artResult.getName()).isEqualTo(a.getName());
    assertThat(artResult.getDescription()).isEqualTo(a.getDescription());
    assertThat(artResult.getImgUrl()).isEqualTo(a.getImgUrl());
    verify(artRepo, times(1)).findById("1250808601744904192");
  }
  @Test
  void testFindByIdNotFound() {
    //given
    given(artRepo.findById(Mockito.anyString())).willReturn(Optional.empty());
    //when
    Throwable thrown = catchThrowable(() -> {
      Art result = artService.findById("1250808601744904192");
    });
    //then
    assertThat(thrown)
            .isInstanceOf(ObjectNotFoundException.class)
            .hasMessage("Could not find art with Id 1250808601744904192");
    verify(artRepo, times(1)).findById("1250808601744904192");
  }
  @Test
  void testFindAllSuccess () {
    //given
    given(artRepo.findAll()).willReturn(arts);
    //when
    List<Art> artList = artService.findAll();
    //then
    assertThat(artList.size()).isEqualTo(2);
    verify(artRepo, times(1)).findAll();
  }

  @Test
  void testCreateArtSuccess() {
    // given
    Art art = new Art();
    art.setName("added artifact");
    art.setDescription("new 마술 기술");
    art.setImgUrl("imageUrl");
    given(idWorker.nextId()).willReturn(123456L);
    given(artRepo.save(art)).willReturn(art);
    // when
    Art createdArt = artService.createArt(art);
    // then
    assertThat(createdArt.getId()).isEqualTo("123456");
    assertThat(createdArt.getName()).isEqualTo("added artifact");
    assertThat(createdArt.getDescription()).isEqualTo("new 마술 기술");
    verify(artRepo, times(1)).save(art);
  }
  @Test
  void testUpdateSuccess() {
    Art oldArt = new Art();
    oldArt.setId("1250808601744904191");
    oldArt.setName("Deluminator");
    oldArt.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
    oldArt.setImgUrl("ImageUrl");

    Art newArt = new Art();
    newArt.setName("Deluminator");
    newArt.setDescription("A Deluminator is UPDATED");
    newArt.setImgUrl("ImageUrl");

    given(artRepo.findById("1250808601744904191")).willReturn(Optional.of(oldArt));
    given(artRepo.save(oldArt)).willReturn(oldArt);
    // when
    Art updateArt = artService.updateArt(oldArt.getId(), newArt);
    // then
    assertThat(updateArt.getId()).isEqualTo(updateArt.getId());
    assertThat(updateArt.getName()).isEqualTo(updateArt.getName());
    assertThat(updateArt.getDescription()).isEqualTo(updateArt.getDescription());
    verify(artRepo, times(1)).findById("1250808601744904191");
    verify(artRepo, times(1)).save(oldArt);

  }
  @Test
  void testUpdateNotFound(){
    // Given
    Art update = new Art();
    update.setId("1250808601744904191");
    update.setName("Deluminator");
    update.setDescription("A Deluminator is UPDATE");
    update.setImgUrl("ImageUrl");
    given(artRepo.findById("1250808601744904191")).willReturn(Optional.empty());
    // When
    assertThrows(ObjectNotFoundException.class, () -> {
      artService.updateArt("1250808601744904191", update);
    });
    // Then
    verify(artRepo, times(1)).findById("1250808601744904191");
  }
  @Test
  void testDeleteSuccess() {
    // Given
    Art art = new Art();
    art.setId("1250808601744904191");
    art.setName("Deluminator");
    art.setDescription("A Deluminator is UPDATE");
    art.setImgUrl("ImageUrl");

    given(artRepo.findById("1250808601744904191")).willReturn(Optional.of(art));
    doNothing().when(this.artRepo).deleteById("1250808601744904191");
    // when
    artService.deleteArt("1250808601744904191");

    // Then
    verify(artRepo, times(1)).findById("1250808601744904191");
    verify(artRepo, times(1)).deleteById("1250808601744904191");
  }
  @Test
  void testDeleteNotFound() {
    // Given
    Art art = new Art();
    art.setId("1250808601744904191");
    art.setName("Deluminator");
    art.setDescription("A Deluminator is UPDATE");
    art.setImgUrl("ImageUrl");

    given(artRepo.findById("1250808601744904191")).willReturn(Optional.empty());
    // when
    assertThrows(ObjectNotFoundException.class, () -> {
      artService.deleteArt("1250808601744904191");
    });

    // Then
    verify(artRepo, times(1)).findById("1250808601744904191");
  }

}