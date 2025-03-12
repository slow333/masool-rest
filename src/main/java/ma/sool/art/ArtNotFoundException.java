package ma.sool.art;

public class ArtNotFoundException extends RuntimeException {
  public ArtNotFoundException(String id) {
    super("Could not find art with Id "+id);
  }
}
