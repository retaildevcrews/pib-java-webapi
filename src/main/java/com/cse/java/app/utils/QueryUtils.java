package com.cse.java.app.utils;

import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * Query Utilities.
 */
public class QueryUtils {

  private QueryUtils() {
  }

  /**
   * Detects category, sub-category and mode from request URI.
   * 
   * @param request HttpRequest
   * @return String Array with category, subCategory and mode in that order
   */
  public static String[] getCategoryAndMode(ServerHttpRequest request) {

    String category;
    String mode;
    String subCategory;
    String path = request.getURI().getPath().toLowerCase();
    if (path.startsWith("/healthz")) {
      category = "Healthz";
      subCategory = "Healthz";
      mode = "Healthz";
    } else if (path.startsWith("/metrics")) {
      category = "Metrics";
      subCategory = "Metrics";
      mode = "Metrics";
    } else {
      category = "Static";
      subCategory = "Static";
      mode = "Static";
    }

    return new String[] { category, subCategory, mode };
  }

  /**
   * Http status code to Prometheus string code.
   * 
   * @param statusCode Http Status Code
   * @return String Prometheus string code
   */
  public static String getPrometheusCode(int statusCode) {

    if (statusCode >= 500) {
      return "Error";
    } else if (statusCode == 429) {
      return "Retry";
    } else if (statusCode >= 400) {
      return "Warn";
    }

    return "OK";
  }
}
