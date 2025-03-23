package ma.sool.hoguser;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.sool.system.Result;
import ma.sool.system.StatusCode;
import ma.sool.system.converter.UserToDtoConverter;
import ma.sool.system.converter.UserToEntityConverter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.base-url}/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserToDtoConverter userToDtoConverter;
  private final UserToEntityConverter userToEntityConverter;

  @GetMapping
  public Result findAllUsers() {
    List<HogUser> foundUsers = userService.findAll();
    List<UserDto> userDtos = foundUsers.stream()
            .map(userToDtoConverter::convert)// method...
            .toList();
    return new Result(true, StatusCode.SUCCESS, "Find All Success", userDtos);
  }

  @GetMapping("/{userId}")
  public Result findById(@PathVariable Long userId) {
    HogUser foundUser = userService.findById(userId);
    UserDto convertedUserDto = userToDtoConverter.convert(foundUser);
    return new Result(true, StatusCode.SUCCESS, "Find Id Success", convertedUserDto);
  }

  @PostMapping
  public Result addUser(@Valid @RequestBody HogUser newHogUser) {
    HogUser savedUser = userService.addUser(newHogUser);
    UserDto userDto = userToDtoConverter.convert(savedUser);
    return new Result(true, StatusCode.SUCCESS, "Add Success", userDto);
  }

  @PutMapping("/{userId}")
  public Result updateUser(@PathVariable Long userId, @Valid @RequestBody UserDto userDto) {
    HogUser convertedUser = userToEntityConverter.convert(userDto);
    HogUser updatedUser = userService.update(userId, convertedUser);
    UserDto convertedDto = userToDtoConverter.convert(updatedUser);
    return new Result(true, StatusCode.SUCCESS, "Update Success", convertedDto);
  }

  @DeleteMapping("/{userId}")
  public Result deleteUser(@PathVariable Long userId) {
    userService.deleteById(userId);
    return new Result(true, StatusCode.SUCCESS, "Delete Success");
  }
}
