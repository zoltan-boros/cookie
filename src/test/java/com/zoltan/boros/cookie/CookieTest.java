package com.zoltan.boros.cookie;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.MalformedURLException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Cookie")
class CookieTest {

  @Nested
  @DisplayName("HTTP response header string parsing")
  class HeaderStringParser {

    @Test
    @DisplayName("Should throw if cookie string is empty")
    void shouldThrowIfCookieStringIsEmpty()
    {
      String emptyCookieString = "";
      assertThrows(IllegalCookieStringException.class,
        () -> Cookie.fromHttpResponseHeaderString(emptyCookieString,
          "https://example.com"));
    }

    @Test
    @DisplayName("Should understand empty value with \"=\" character.")
    void shouldUnderstandEmptyValueWithEqualsCharacter()
      throws MalformedURLException, IllegalCookieStringException
    {
      String cookieStringWithEmptyValueWithEquals = "abc=";
      String someRequestUrl = "https://example.com/";
      assertThat(
        Cookie.fromHttpResponseHeaderString(
          cookieStringWithEmptyValueWithEquals, someRequestUrl),
        equalTo(new Cookie("abc", "", "example.com", false)));
    }

    @Test
    @DisplayName("Should understand empty value without \"=\" character.")
    void shouldUnderstandEmptyValueWithoutEqualsCharacter()
      throws MalformedURLException, IllegalCookieStringException
    {
      String cookieStringWithEmptyValueWithoutEquals = "abc";
      String someRequestUrl = "https://example.com/";
      assertThat(
        Cookie.fromHttpResponseHeaderString(
          cookieStringWithEmptyValueWithoutEquals, someRequestUrl),
        equalTo(new Cookie("abc", "", "example.com", false)));
    }

    @Test
    @DisplayName("If \"Domain\" attribute is absent then domain should be set to request URL's host, without subdomains, moreover it should not apply to subdomains.")
    void domainShouldDefaultToRequestUrlsHost()
      throws MalformedURLException, IllegalCookieStringException
    {
      String cookieStringWithoutDomain = "someName=someValue";
      String someRequestUrlWithSubdomainAndPath = "https://some-subdomain.example.com/pathSegment/index.html";
      assertThat(
        Cookie.fromHttpResponseHeaderString(cookieStringWithoutDomain,
          someRequestUrlWithSubdomainAndPath),
        equalTo(new Cookie("someName", "someValue", "example.com", false)));
    }

    @Test
    @DisplayName("If multiple domain attributes are present, then only the first one should be taken into account.")
    void ifMultipleDomainsArePresentThenOnlyTheFirstOneShouldBeTakenIntoAccount()
      throws MalformedURLException, IllegalCookieStringException
    {
      String cookieStringWithMultipleDomains = "someName=someValue; Domain=one.com; Domain=two.com";
      String someRequestUrl = "https://example.com/";
      assertThat(
        Cookie.fromHttpResponseHeaderString(cookieStringWithMultipleDomains,
          someRequestUrl),
        equalTo(new Cookie("someName", "someValue", "one.com", true)));
    }
  }
}
