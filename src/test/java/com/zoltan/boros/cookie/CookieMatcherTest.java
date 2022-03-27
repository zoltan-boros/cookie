package com.zoltan.boros.cookie;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.net.MalformedURLException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Cookie Matcher")
class CookieMatcherTest {

  @Test
  @Disabled
  @DisplayName("Should match even if cookie domain starts in dot.")
  void shouldMatchEvenIfCookieDomainStartsInDot() throws MalformedURLException
  {
    assertThat(
      CookieMatcher.matches("https://example.com/",
        new Cookie("someName", "someValue", ".example.com", true)),
      equalTo(true));
  }

  @Test
  @Disabled
  @DisplayName("Should not match subdomain if cookie does not apply to subdomains.")
  void shouldNotMatchSubdomainIfCookieDoesNotApplyToSubdomains()
    throws MalformedURLException
  {
    assertThat(
      CookieMatcher.matches("https://subdomain.example.com/",
        new Cookie("someName", "someValue", "example.com", false)),
      equalTo(false));
  }

  @Test
  @Disabled
  @DisplayName("Should not match if domain segments are not equal.")
  void shouldNotMatchIfDomainSegmentsAreNotEqual() throws MalformedURLException
  {
    assertThat(CookieMatcher.matches("https://foobar.com/",
      new Cookie("someName", "someValue", "bar.com", true)), equalTo(false));
  }

  @Test
  @Disabled
  @DisplayName("Should not match if subdomain segments are not equal.")
  void shouldNotMatchIfSubdomainSegmentsAreNotEqual()
    throws MalformedURLException
  {
    assertThat(
      CookieMatcher.matches("https://foobar.example.com/",
        new Cookie("someName", "someValue", "bar.example.com", true)),
      equalTo(false));
  }

  @Test
  @DisplayName("Should match subdomain if cookie applies to subdomains.")
  void shouldMatchSubdomainIfCookieAppliesToSubdomains()
    throws MalformedURLException
  {
    String url = "https://subdomain.example.com/";
    Cookie cookie = new Cookie("someName", "someValue", "example.com", true);
    boolean matches = CookieMatcher.matches(url, cookie);
    assertThat(matches, equalTo(true));
  }

  @Test
  @Disabled
  @DisplayName("Should not match to different path.")
  void shouldNotMatchToDifferentPath() throws MalformedURLException
  {
    assertThat(
      CookieMatcher.matches("https://example.com/bar",
        new Cookie("someName", "someValue", "example.com", "/foo", true)),
      equalTo(false));
  }

  @Test
  @Disabled
  @DisplayName("Should not match if cookie path segment is substring.")
  void shouldNotMatchIfCookiePathSegmentIsSubstring()
    throws MalformedURLException
  {
    assertThat(
      CookieMatcher.matches("https://example.com/foobar",
        new Cookie("someName", "someValue", "example.com", "/foo", true)),
      equalTo(false));
  }

  @Test
  @Disabled
  @DisplayName("Should match even if path ends in slash.")
  void shouldMatchEvenIfPathEndsInSlash() throws MalformedURLException
  {
    assertThat(
      CookieMatcher.matches("https://example.com/foo/",
        new Cookie("someName", "someValue", "example.com", "/foo", true)),
      equalTo(true));
  }

  @Test
  @Disabled
  @DisplayName("Should match to subfolder.")
  void shouldMatchToSubfolder() throws MalformedURLException
  {
    assertThat(
      CookieMatcher.matches("https://example.com/foo/bar",
        new Cookie("someName", "someValue", "example.com", "/foo", false)),
      equalTo(true));
  }

  @Test
  @Disabled
  @DisplayName("Should match to subdomain's subfolder.")
  void shouldMatchToSubdomainsSubfolder() throws MalformedURLException
  {
    assertThat(
      CookieMatcher.matches("https://subdomain.example.com/foo/bar",
        new Cookie("someName", "someValue", "example.com", "/foo", true)),
      equalTo(true));
  }
}
