package com.cse.java.app.utils;

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
  
  InvalidParameterResponses() {
  }

  private String response(String validationErrorsBody) {
    return "{\n"
      + "  \"title\": \"Parameter validation error\",\n"
      + "  \"detail\": \"One or more invalid parameters were specified.\",\n"
      + "  \"status\": 400,\n"
      + "  \"validationErrors\": [\n"
      +    validationErrorsBody + "\n"
      + "  ]\n"
      + "}";
  }

  public String invalidBenchmarkSizeResponse() {
    return response(BENCHMARK_SIZE_ERROR);
  }
  
  public String invalidSecretKey(String message) {
    return secretNotFoundResponse(message);
  }
  
  private String secretNotFoundResponse(String message) {
    return "{\n"
      + "  \"title\": \"Secret not found\",\n"
      + "  \"detail\": \"" + message + "\",\n"
      + "  \"status\": 404"
      + "}";
  }
  
}
