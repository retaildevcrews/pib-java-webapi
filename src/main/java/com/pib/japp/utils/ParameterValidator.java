// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See LICENSE in the project root for license information.

package com.pib.japp.utils;

import org.springframework.stereotype.Component;

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

  /** isValidBenchmarkSize. */
  public Boolean isValidBenchmarkSize(int benchmarkSize, int maxBenchmarkSize) {
    if (benchmarkSize < 1 || benchmarkSize > maxBenchmarkSize) {
      return false;
    }
    return true;
  }

}
