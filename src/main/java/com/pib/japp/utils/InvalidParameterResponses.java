// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See LICENSE in the project root for license information.

package com.pib.japp.utils;

import org.springframework.stereotype.Component;

@Component
public class InvalidParameterResponses {

  private static final String VALIDATION_ERROR_TEMPLATE = "    {\n"
      + "      \"code\": \"InvalidValue\",\n"
      + "      \"target\": \"%s\",\n"
      + "      \"message\": \"%s\"\n"
      + "    }";

  private static final String BENCHMARK_SIZE_ERROR = String.format(
      VALIDATION_ERROR_TEMPLATE,
      "benchmarkSize",
      "The parameter 'benchmarkSize' should be a number greater than 0 and less than 1024*1024"
      );
  
  private static final String INVALID_PARAMETER__TYPE = "ParameterValidationError";
  
  InvalidParameterResponses() {
  }

  private String response(String type, String instance, String validationErrorsBody) {
    return "{\n"
      + "  \"type\": \"" + type + "\",\n"
      + "  \"title\": \"Parameter validation error\",\n"
      + "  \"detail\": \"One or more invalid parameters were specified.\",\n"
      + "  \"status\": 400,\n"
      + "  \"instance\": \"" + instance + "\",\n"
      + "  \"validationErrors\": [\n"
      +    validationErrorsBody + "\n"
      + "  ]\n"
      + "}";
  }

  public String invalidBenchmarkSizeResponse(String instance) {
    return response(INVALID_PARAMETER__TYPE, instance, BENCHMARK_SIZE_ERROR);
  }
  
}
