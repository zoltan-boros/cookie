package com.zoltan.boros.cookie;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class Cookie {

  String name;
  String value;
  String domain;
  String path;
  boolean appliesToSubDomains;
  boolean secure;
  boolean httpOnly;

  Cookie()
  {
  }

  Cookie(String name, String value, String domain, boolean appliesToSubDomains)
  {
    this(name, value, domain, "", appliesToSubDomains);
  }

  Cookie(
    String name, String value, String domain, String path,
    boolean appliesToSubDomains
  )
  {
    this.name = name;
    this.value = value;
    this.domain = domain;
    this.path = path;
    this.appliesToSubDomains = appliesToSubDomains;
  }

  /**
   * Meant to create <code>Cookie</code> instance from HTTP response
   * "Set-Cookie" header string.
   */
  public static Cookie fromHttpResponseHeaderString(
    String headerString,
    String requestUrl
  ) throws IllegalCookieStringException, MalformedURLException
  {
    if (headerString.isBlank()) {
      throw new IllegalCookieStringException(String
        .format("Illegal http header cookie string: \"%s\"", headerString));
    }

    Cookie cookie = new Cookie();

    List<String[]> pairs = Arrays.stream(headerString.split(";"))
      .map(String::strip).map(nameValue -> nameValue.split("=", 2))
      .collect(toList());

    String[] nameAndValue = pairs.get(0);
    cookie.name = nameAndValue[0];
    cookie.value = nameAndValue.length == 2 ? nameAndValue[1] : "";

    Map<String, List<String>> attributes = pairs.subList(1, pairs.size())
      .stream()
      .collect(toMap(nameValue -> nameValue[0].toLowerCase(),
        nameValue -> List.of(nameValue.length == 2 ? nameValue[1] : ""),
        (a, b) -> Stream.concat(a.stream(), b.stream()).collect(toList())));

    if (attributes.containsKey("domain")) {
      cookie.domain = attributes.get("domain").get(0);
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

    cookie.path = attributes.getOrDefault("path", List.of("")).get(0);

    cookie.secure = attributes.containsKey("secure");
    cookie.httpOnly = attributes.containsKey("httponly");

    return cookie;
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(appliesToSubDomains, domain, httpOnly, name, path,
      secure, value);
  }

  @Override
  public boolean equals(Object obj)
  {
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
  public String toString()
  {
    return "Cookie [name=" + name + ", value=" + value + ", domain=" + domain
      + ", path=" + path + ", appliesToSubDomains=" + appliesToSubDomains
      + ", secure=" + secure + ", httpOnly=" + httpOnly + "]";
  }
}
