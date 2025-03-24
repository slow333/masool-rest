package ma.sool.wiz;

import jakarta.validation.Valid;
import ma.sool.system.Result;
import ma.sool.system.StatusCode;
import ma.sool.system.converter.WizToDtoConverter;
import ma.sool.system.converter.WizToEntityConverter;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("${api.base-url}/wizs")
public class WizController {

  private final WizService wizService;
  private final WizToDtoConverter wizToDtoConverter;
  private final WizToEntityConverter wizToEntityConverter;

  public WizController(WizService wizService, WizToDtoConverter wizToDtoConverter, WizToEntityConverter wizToEntityConverter) {
    this.wizService = wizService;
    this.wizToDtoConverter = wizToDtoConverter;
    this.wizToEntityConverter = wizToEntityConverter;
  }

  @GetMapping("/{wizId}")
  public Result findArtById(@PathVariable Integer wizId) {
    Wiz wizResult = wizService.findById(wizId);
    WizDto wizDto = wizToDtoConverter.convert(wizResult);
    return new Result(true, StatusCode.SUCCESS, "Find One Success", wizDto);
  }

  @GetMapping
  public Result findAll() {
    List<Wiz> wizs = wizService.findAll();
    List<WizDto> wizDtos = wizs.stream().map(wizToDtoConverter::convert).toList();
    return new Result(true, StatusCode.SUCCESS, "Find All Success", wizDtos);
  }

  @PostMapping
  public Result addWiz(@Valid @RequestBody WizDto wizDto) {
    Wiz convertedWizEntity = wizToEntityConverter.convert(wizDto);
    Wiz savedWiz = wizService.saveWiz(convertedWizEntity);
    WizDto convertDto = wizToDtoConverter.convert(savedWiz);
    return new Result(true, StatusCode.SUCCESS, "Add Success", convertDto);
  }

  @PutMapping("/{wizId}")
  public Result updateWiz(@PathVariable Integer wizId, @Valid @RequestBody WizDto wizDto) {
    Wiz convertWiz = wizToEntityConverter.convert(wizDto);
    Wiz updateWiz = wizService.updateWiz(wizId, convertWiz);
    WizDto convertDto = wizToDtoConverter.convert(updateWiz);
    return new Result(true, StatusCode.SUCCESS, "Update Success", convertDto);
  }

  @DeleteMapping("/{wizId}")
  public Result deleteWiz(@PathVariable Integer wizId) {
    wizService.deleteWiz(wizId);
    return new Result(true, StatusCode.SUCCESS, "Delete Success");
  }

  @PutMapping("/{wizId}/arts/{artId}")
  public Result changeArtOwner(@PathVariable Integer wizId, @PathVariable String artId) {
    wizService.changeArtOwner(wizId, artId);
    return new Result(true, StatusCode.SUCCESS, "Art Change owner Success");
  }
}
