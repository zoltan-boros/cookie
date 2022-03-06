package com.zoltan.boros.cookie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.MalformedURLException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Cookie")
class CookieTest {

//  private static final String SOME_URL = "https://example.com/foo/bar/index.php?n1=v2&n2=v2";

  @Nested
  @DisplayName("HTTP response header string parsing")
  class HeaderStringParser {

    @Test
    @DisplayName("Should throw if cookie string is empty")
    void shouldThrowIfCookieStringIsEmpty() {
      assertThrows(IllegalCookieStringException.class,
        () -> Cookie.fromHttpResponseHeaderString("", "https://example.com"));
    }

    @Test
    @DisplayName("Should understand empty value with \"=\" character.")
    void shouldUnderstandEmptyValueWithEqualsCharacter()
      throws MalformedURLException, IllegalCookieStringException {
      assertEquals(new Cookie("abc", "", "example.com", false),
        Cookie.fromHttpResponseHeaderString("abc=", "https://example.com"));
    }

    @Test
    @DisplayName("Should understand empty value without \"=\" character.")
    void shouldUnderstandEmptyValueWithoutEqualsCharacter()
      throws MalformedURLException, IllegalCookieStringException {
      assertEquals(new Cookie("abc", "", "example.com", false),
        Cookie.fromHttpResponseHeaderString("abc", "https://example.com"));
    }

    @Test
    @DisplayName("If \"Domain\" property is absent then domain should be set based on request URL's host.")
    void domainShouldDefaultToRequestUrlsHost()
      throws MalformedURLException, IllegalCookieStringException {
      assertEquals(new Cookie("name1", "value1", "example.com", false),
        Cookie.fromHttpResponseHeaderString("name1=value1",
          "https://some-sub-domain.example.com/pathSegment/index.html"));
    }
  }
}
