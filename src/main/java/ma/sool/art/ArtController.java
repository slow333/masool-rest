package ma.sool.art;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.sool.system.converter.ArtToDtoConverter;
import ma.sool.system.Result;
import ma.sool.system.StatusCode;
import ma.sool.system.converter.ArtToEntityConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.base-url}/arts")
@RequiredArgsConstructor
public class ArtController {

  private final ArtService artService;
  private final ArtToDtoConverter artToDtoConverter;
  private final ArtToEntityConverter artToEntityConverter;
  private final MeterRegistry meterRegistry;

  @GetMapping("/{artId}")
  public Result findArtById(@PathVariable String artId) {
    Art result = artService.findById(artId);
    meterRegistry.counter("artifact.id." + artId).increment();
    ArtDto artDto = artToDtoConverter.convert(result);
    return new Result(true, StatusCode.SUCCESS, "Find One Success", artDto);
  }

  @GetMapping
  public Result findAllArts(@PageableDefault(size = 2, page = 1) Pageable pageable) { // page;0, size;20, sort;unsorted
    Page<Art> artPage = artService.findAll(pageable); // Page streamable no need stream()
    Page<ArtDto> artDtoPage = artPage.map(artToDtoConverter::convert);
    return new Result(true, StatusCode.SUCCESS, "Find All Success", artDtoPage);
  }

  @PostMapping
  public Result createArt(@Valid @RequestBody ArtDto artDto) {
    Art art = artToEntityConverter.convert(artDto);
    Art addedArt = artService.createArt(art);
    ArtDto artDtoResult = artToDtoConverter.convert(addedArt);
    return new Result(true, StatusCode.SUCCESS, "Add Success", artDtoResult);
  }

  @PutMapping("/{artId}")
  public Result updateArt(@PathVariable String artId, @Valid @RequestBody ArtDto artDto) {
    Art newArt = artToEntityConverter.convert(artDto);
    Art responseArt = artService.updateArt(artId, newArt);
    ArtDto convertedArtDto = artToDtoConverter.convert(responseArt);
    return new Result(true, StatusCode.SUCCESS, "Update Success", convertedArtDto);
  }

  @DeleteMapping("/{artId}")
  public Result deleteArt(@PathVariable String artId) {
    artService.deleteArt(artId);
    return new Result(true, StatusCode.SUCCESS, "Delete Success");
  }


}
