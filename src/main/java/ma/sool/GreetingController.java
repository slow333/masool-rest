package ma.sool;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

  @GetMapping("/greeting")
  public String hello(
          @RequestParam(name = "wizName", required = false, defaultValue = "iam") String name){

    return "Hello " + name +" !!!";
  }
}
