package ma.sool.wiz;

public class WizNotFoundException extends RuntimeException {
  public WizNotFoundException(Integer id) {
    super("Could not find wiz with Id "+id);
  }
}
