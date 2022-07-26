package com.cse.java.app.controllers;


import com.cse.java.app.Constants;
import com.cse.java.app.utils.InvalidParameterResponses;
import com.cse.java.app.utils.ParameterValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/benchmark", produces = {MediaType.TEXT_PLAIN_VALUE,
    MediaType.APPLICATION_PROBLEM_JSON_VALUE})
@Api(tags = "Benchmark")
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
  public Mono<ResponseEntity<String>> getBenchmark(
      @ApiParam(value = "The size of the benchmark data ( 0 < size <= 1MB )",
                example = "214", required = true)
      @PathVariable("size")
      String benchmarkSizeStr
  ) {

    if (Boolean.TRUE == validator.isValidBenchmarkSize(benchmarkSizeStr, Constants.MAX_BENCH_STR_SIZE)) {

      int benchmarkSize = Integer.parseInt(benchmarkSizeStr);
      return Mono.justOrEmpty(ResponseEntity.ok(
          benchmarkString.substring(0, benchmarkSize)));

    } else {

      String invalidResponse = invalidParameterResponses
          .invalidBenchmarkSizeResponse();
      logger.warn("Benchmark data size parameter should be 0 < size <= 1MiB (1048576)");

      return Mono.just(ResponseEntity.badRequest()
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(invalidResponse));
    }
  }
}
