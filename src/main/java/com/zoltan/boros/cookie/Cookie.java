package com.zoltan.boros.cookie;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Cookie {

  String name;
  String value;
  String domain;
  String path;
  boolean appliesToSubDomains;
  boolean secure;
  boolean httpOnly;

  public Cookie() {
  }

  public Cookie(
    String name, String value, String domain, boolean appliesToSubDomains
  ) {
    this.name = name;
    this.value = value;
    this.domain = domain;
    this.appliesToSubDomains = appliesToSubDomains;
  }

  /**
   * Meant to create <code>Cookie</code> instance from HTTP response
   * "Set-Cookie" header string.
   */
  public static Cookie fromHttpResponseHeaderString(
    String headerString,
    String requestUrl
  ) throws IllegalCookieStringException, MalformedURLException {
    if (headerString.isBlank()) {
      throw new IllegalCookieStringException(String
        .format("Illegal http header cookie string: \"%s\"", headerString));
    }

    Cookie cookie = new Cookie();

    List<String[]> allProps = Arrays.stream(headerString.split(";"))
      .map(String::strip).map(nameValue -> nameValue.split("=", 2))
      .collect(Collectors.toList());

    String[] nameAndValue = allProps.get(0);
    cookie.name = nameAndValue[0];
    cookie.value = nameAndValue.length == 2 ? nameAndValue[1] : "";

    List<String[]> furtherProps = allProps.subList(1, allProps.size());
    Map<String, String> props = furtherProps.stream()
      .collect(Collectors.toMap(nameValue -> nameValue[0].toLowerCase(),
        nameValue -> nameValue.length == 2 ? nameValue[1] : ""));

    if (props.containsKey("domain")) {
      cookie.domain = props.get("domain");
      if (cookie.domain.endsWith("/")) {
        cookie.domain = cookie.domain.substring(0, cookie.domain.length() - 1);
      }
      cookie.appliesToSubDomains = true;
    } else {
      String[] hostComponents = new URL(requestUrl).getHost().split("\\.");
      int hcLength = hostComponents.length;
      cookie.domain = String.join(".",
        Arrays.asList(hostComponents).subList(hcLength - 2, hcLength));
      cookie.appliesToSubDomains = false;
    }

    cookie.path = props.get("path");

    cookie.secure = props.containsKey("secure");
    cookie.httpOnly = props.containsKey("httponly");

    return cookie;
  }

  public boolean appliesTo(String urlString) throws MalformedURLException {
    // cookie: example.com/
    // url: example.com/foo?...
    URL url = new URL(urlString);
    // ToDo: Take this.appliesToSubDomains into account.
    // ToDo: Wrong because it should not accept some-example.com
    // if cookie's domain is example.com
    // Analogously fix path check.
    return url.getHost().endsWith(domain) && url.getPath().startsWith(path);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appliesToSubDomains, domain, httpOnly, name, path,
      secure, value);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Cookie other = (Cookie) obj;
    return appliesToSubDomains == other.appliesToSubDomains
      && Objects.equals(domain, other.domain) && httpOnly == other.httpOnly
      && Objects.equals(name, other.name) && Objects.equals(path, other.path)
      && secure == other.secure && Objects.equals(value, other.value);
  }

  @Override
  public String toString() {
    return "Cookie [name=" + name + ", value=" + value + ", domain=" + domain
      + ", path=" + path + ", appliesToSubDomains=" + appliesToSubDomains
      + ", secure=" + secure + ", httpOnly=" + httpOnly + "]";
  }

//  public static void main(String[] args)
//    throws MalformedURLException, IllegalCookieFormatException {
////    URL url = new URL("https://foo.bar.com/abc/def/ghi.html?n1=v1&n2=v2");
////    System.out.println("Host: " + url.getHost());
////    System.out.println("Path: " + url.getPath());
////    System.out.println("File: " + url.getFile());
////    System.out.println("Query String: " + url.getQuery());
////    Cookie cookie = Cookie.fromHttpResponseHeaderString("cookie1=value1",
////      "https://sub-domain.example.com/foo/bar/index.html?n1=v1&n2=v2");
//    Cookie cookie = Cookie.fromHttpResponseHeaderString(
//      "cookie1=value1; Domain=example.com; Path=/foo", "");
//    System.out.println("Cookie Domain: " + cookie.domain);
//    System.out.println("Cookie Path: " + cookie.path);
//    System.out.println(cookie.appliesTo("https://example.com/bar?n=v"));
//  }
}
