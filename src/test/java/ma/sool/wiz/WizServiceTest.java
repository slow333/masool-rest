package ma.sool.wiz;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WizServiceTest {

  @Mock
  WizRepo wizRepo;
  @InjectMocks
  WizService wizService;

  @BeforeEach
  void setUp() {
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
    assertThat(thrown).isInstanceOf(WizNotFoundException.class);
    verify(wizRepo, times(1)).findById(1);

  }
}