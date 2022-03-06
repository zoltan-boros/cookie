package com.zoltan.boros.cookie;

public class IllegalCookieStringException extends Exception {

  private static final long serialVersionUID = 8130246524911843668L;

  public IllegalCookieStringException(String message) {
    super(message);
  }

  public IllegalCookieStringException(String message, Throwable cause) {
    super(message, cause);
  }
}
