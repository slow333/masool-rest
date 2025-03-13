package ma.sool.wiz;

import jakarta.el.MethodNotFoundException;
import ma.sool.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WizServiceTest {

  @Mock
  WizRepo wizRepo;
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
}