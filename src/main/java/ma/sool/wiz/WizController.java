package ma.sool.wiz;

import ma.sool.system.Result;
import ma.sool.system.StatusCode;
import ma.sool.system.converter.WizToDtoConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("${api.base-url}/wizs")
public class WizController {

  private final WizService wizService;
  private final WizToDtoConverter wizToDtoConverter;

  public WizController(WizService wizService, WizToDtoConverter wizToDtoConverter) {
    this.wizService = wizService;
    this.wizToDtoConverter = wizToDtoConverter;
  }

  @GetMapping("/{wizId}")
  public Result findArtById(@PathVariable Integer wizId) {
    Wiz wizResult = wizService.findById(wizId);
    WizDto wizDto = wizToDtoConverter.convert(wizResult);
    return new Result(true, StatusCode.SUCCESS, "Find One Success", wizDto);
  }
}
