// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See LICENSE in the project root for license information.

package com.pib.japp.controllers;

import com.pib.japp.Constants;
import com.pib.japp.utils.InvalidParameterResponses;
import com.pib.japp.utils.ParameterValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/benchmark", produces = {MediaType.TEXT_PLAIN_VALUE,
    MediaType.APPLICATION_PROBLEM_JSON_VALUE})
@Tag(name = "Benchmark")
public class BenchmarkController {

  private static final Logger logger = LogManager.getLogger(BenchmarkController.class);

  private final String benchmarkString;

  @Autowired
  ParameterValidator validator;

  @Autowired
  InvalidParameterResponses invalidParameterResponses;

  /** BenchmarkController constructor. */
  public BenchmarkController() {
    var initialStr = "0123456789ABCDEF";
    benchmarkString = initialStr.repeat(Constants.MAX_BENCH_STR_SIZE / initialStr.length() + 1);
  }

  /** getBenchmark. */
  @GetMapping(value = "/{size}")
  @Operation(summary = "Returns a string value of benchmark data")
  @SuppressWarnings({"squid:S2629", "squid:S1612"})
  public Mono<ResponseEntity<String>> getBenchmark(
      @PathVariable("size")
      String benchmarkSizeStr,
      ServerHttpRequest request
  ) {

    if (Boolean.TRUE == validator.isValidBenchmarkSize(benchmarkSizeStr, Constants.MAX_BENCH_STR_SIZE)) {

      int benchmarkSize = Integer.parseInt(benchmarkSizeStr);
      return Mono.justOrEmpty(ResponseEntity.ok(
          benchmarkString.substring(0, benchmarkSize)));

    } else {

      String invalidResponse = invalidParameterResponses
          .invalidBenchmarkSizeResponse(request.getURI().getPath());
      logger.warn("Benchmark data size parameter should be 0 < size <= 1MiB (1048576)");

      return Mono.just(ResponseEntity.badRequest()
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(invalidResponse));
    }
  }
}
