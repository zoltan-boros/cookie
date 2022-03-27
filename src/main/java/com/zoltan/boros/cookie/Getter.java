package com.zoltan.boros.cookie;

import java.util.ArrayList;
import java.util.List;

public class Getter {

  private String url;
  private List<Cookie> cookies;

  public Getter(String url) {
    this(url, new ArrayList<>(100));
  }

  public Getter(String url, List<Cookie> cookies) {
    this.url = url;
    this.cookies = cookies;
  }
}
