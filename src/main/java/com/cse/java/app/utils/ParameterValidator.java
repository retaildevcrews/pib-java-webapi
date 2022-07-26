package com.cse.java.app.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/** ParameterValidator. */
@Component
public class ParameterValidator {

  /**
   * This method will return true if input is int and false otherwise.
   * 
   * @param s input string to test as an int
   * @return boolean
   */
  public static boolean isInteger(String s) {
    boolean isValidInteger = false;
    try {
      Integer.parseInt(s);
      isValidInteger = true;
    } catch (NumberFormatException ex) {
      isValidInteger = false;
    }

    return isValidInteger;
  }

  /** isValidRating. */
  public Boolean isValidBenchmarkSize(String benchmarkSizeStr, int maxBenchStrSize) {
    if (!StringUtils.isEmpty(benchmarkSizeStr)) {
      try {
        int benchmarkSize = Integer.parseInt(benchmarkSizeStr);
        if (benchmarkSize < 1 || benchmarkSize > maxBenchStrSize) {
          return false;
        }
      } catch (Exception ex) {
        return false;
      }
    }
    return true;
  }
  
  /** isInvalidSecret. */
  public Boolean isValidSecretKey(String key) {
    if (!StringUtils.isEmpty(key)) {
      return true;
    }
    return false;
  }

}
