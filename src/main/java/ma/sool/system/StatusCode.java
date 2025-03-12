package ma.sool.system;

import lombok.Getter;

public class StatusCode {
  public static final int SUCCESS = 200;
  public static final int NON_CONTENT = 204;
  public static final int INVALID_ARGUMENT = 400;
  public static final int UNAUTHORIZED = 401;
  public static final int FORBIDDEN = 403;
  public static final int NOT_FOUND = 404;
  public static final int INTERNAL_SERVER_ERROR = 500;
}
