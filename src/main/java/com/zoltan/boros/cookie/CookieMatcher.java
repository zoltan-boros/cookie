package com.zoltan.boros.cookie;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CookieMatcher {

  private static String withoutLeadingDot(String url)
  {
    return url.startsWith(".") ? url.substring(1) : url;
  }

  private static <T> List<T> reverse(List<T> list)
  {
    List<T> clone = new ArrayList<>(list);
    Collections.reverse(clone);
    return clone;
  }

  private static <T> boolean startsWith(List<T> list, List<T> prefix)
  {
    if (list.size() < prefix.size()) {
      return false;
    }
    for (int i = 0; i < prefix.size(); i++) {
      if (!list.get(i).equals(prefix.get(i))) {
        return false;
      }
    }
    return true;
  }

  private static <T> boolean endsWith(List<T> list, List<T> postfix)
  {
    return startsWith(reverse(list), reverse(postfix));
  }

  private static boolean pathMatches(String urlPath, String cookiePath)
  {
    return startsWith(Arrays.asList(urlPath.split("/")),
      Arrays.asList(cookiePath.split("/")));
  }

  private static boolean domainMatches(String urlDomain, Cookie cookie)
  {
    List<String> cookieDomainSegments = Arrays
      .asList(withoutLeadingDot(cookie.domain).split("\\."));
    List<String> urlDomainSegments = Arrays.asList(urlDomain.split("\\."));
    if (cookie.appliesToSubDomains) {
      return endsWith(urlDomainSegments, cookieDomainSegments);
    }
    return urlDomainSegments.equals(cookieDomainSegments);
  }

  public static boolean matches(String urlString, Cookie cookie)
    throws MalformedURLException
  {
    URL url = new URL(urlString);
    return domainMatches(url.getHost(), cookie)
      && pathMatches(url.getPath(), cookie.path.isEmpty() ? "/" : cookie.path);
  }
}
