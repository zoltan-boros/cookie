package com.zoltan.boros.cookie;

public class IllegalCookieFormatException extends Exception {

  private static final long serialVersionUID = 8130246524911843668L;

  public IllegalCookieFormatException(String message) {
    super(message);
  }

  public IllegalCookieFormatException(String message, Throwable cause) {
    super(message, cause);
  }
}
