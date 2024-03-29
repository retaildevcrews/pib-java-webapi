// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See LICENSE in the project root for license information.

package com.pib.japp.controllers;

import com.pib.japp.Constants;
import com.pib.japp.config.BuildConfig;
import com.pib.japp.health.ietf.IeTfStatus;
import com.pib.japp.utils.CommonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/healthz")
@Tag(name = "Healthz")
public class HealthzController {

  private static final Logger logger =   LogManager.getLogger(HealthzController.class);
  private static final String STATUS_TEXT = "status";


  @Autowired private BuildConfig buildConfig;

  @Autowired
  CommonUtils commonUtils;

  @Autowired
  Environment environment;
  // Used to update start time to calculate duration.
  // Using a list, because the variable must be final when used in the map.
  final List<Long> timeList = new ArrayList<>();

  /**
   * healthCheck.
   *
   * @return responseEntity with status information for overall application health.
   */
  @GetMapping(value = "", produces = MediaType.TEXT_PLAIN_VALUE)
  @Operation(summary = "Returns a plain text health status (pass, warn, or fail)")
  @ApiResponse(responseCode = "200", description = "Success",
               content = @Content(schema = @Schema(type = "string")))
  public Mono<ResponseEntity<String>> healthCheck() {
    logger.info("healthz endpoint");

    Mono<List<Map<String, Object>>> resultsMono = buildHealthCheckChain();
    return resultsMono.map(data -> {
      String healthStatus = getOverallHealthStatus(data);
      int resCode = healthStatus.equals(IeTfStatus.FAIL.name()) ? HttpStatus.SERVICE_UNAVAILABLE.value()
          : HttpStatus.OK.value();
      return new ResponseEntity<>(healthStatus.toLowerCase(), HttpStatus.valueOf(resCode));
    });
  }

  /**
   * ietfhealthCheck runs several checks and returs and overall status and results for
   *    the discrete calls that are made.
   *
   *    @return Mono{@literal <}ResponseEntity{@literal <}LinkedHashMap{@literal <}String,
   *        Object{@literal >}{@literal >}{@literal >}
   *        returned with status information for overall execution and discrete calls.
   */
  @GetMapping(value = "/ietf", produces = "application/health+json")
  @Operation(summary = "Returns an IETF (draft) health+json representation of the full Health Check")
  @ApiResponse(responseCode = "200", description = "Success",
              content = @Content(schema = @Schema(type = "LinkedHashMap")))
  public Mono<ResponseEntity<LinkedHashMap<String, Object>>>  ietfHealthCheck() {
    if (logger.isInfoEnabled()) {
      logger.info("healthz ietf endpoint");
    }

    LinkedHashMap<String, Object> ieTfResult = new LinkedHashMap<>();

    String webInstanceRole = environment.getProperty(Constants.WEB_INSTANCE_ROLE_ID);
    if (webInstanceRole == null || webInstanceRole.isEmpty()) {
      webInstanceRole = "unknown";
    }

    ieTfResult.put("serviceId", environment.getProperty("service.name"));
    ieTfResult.put("description", "Java-App Health Check");
    ieTfResult.put("instance", webInstanceRole);
    ieTfResult.put("version", buildConfig.getBuildVersion());

    Mono<List<Map<String, Object>>> resultsMono = buildHealthCheckChain();
    return resultsMono.map(data -> {
      ieTfResult.put(STATUS_TEXT, getOverallHealthStatus(data));
      Map<String, Object> resultsDictionary = convertResultsListToDictionary(data);

      ieTfResult.put("checks", resultsDictionary);
      return ieTfResult;
    }).map(result -> ResponseEntity.ok().body(result));
  }

  /** buildHelthCheckChain build the chain of calls using concatWith. */
  Mono<List<Map<String, Object>>> buildHealthCheckChain() {
    timeList.add(System.currentTimeMillis());
    try {
      Thread.sleep(10);
    } catch (Exception e) {
      logger.error("failed to wait 10 ms");
    }
    Map<String, Object> benchmarkMono = buildResultsDictionary("getBenchmark", getElapsedAndUpdateStart(), 200L);
    return Mono.just(new ArrayList<>(Arrays.asList(benchmarkMono)));
  }

  Long getElapsedAndUpdateStart() {
    Long start = timeList.get(timeList.size() - 1);
    Long elapsed = System.currentTimeMillis() - start;
    timeList.add(System.currentTimeMillis());
    return elapsed;
  }

  String getOverallHealthStatus(List<Map<String, Object>> resultsList) {
    String returnStatus = IeTfStatus.PASS.name();

    for (Map<String, Object> resultItem : resultsList) {
      if (!resultItem.get(STATUS_TEXT).toString().equalsIgnoreCase(IeTfStatus.PASS.name())) {
        returnStatus = resultItem.get(STATUS_TEXT).toString();
        if (returnStatus.equals(IeTfStatus.FAIL.name())) {
          // if we hit a fail then break otherwise loop to end
          break;
        }
      }
    }
    return returnStatus;
  }

  /** convertResultsListToDictionary converts the list from the chain to a dictionary. */
  Map<String, Object> convertResultsListToDictionary(List<Map<String, Object>> resultsList) {
    Map<String, Object> returnDict = new HashMap<>();

    resultsList.forEach(resultItem -> {
      String keyName = resultItem.get("componentId") + ":responseTime";
      returnDict.put(keyName, resultItem);
    });

    return returnDict;
  }

  /** buildResultsDictionary used to create the discrete results for each call in the chain. */
  Map<String, Object> buildResultsDictionary(String componentId,
      Long duration, Long expectedDuration) {
    String passStatus;
    if (duration <= expectedDuration) {
      passStatus = IeTfStatus.PASS.name();
    } else {
      passStatus = IeTfStatus.WARN.name();
    }

    Map<String, Object> resultsDict = new HashMap<>();
    resultsDict.put("componentId", componentId);
    resultsDict.put("componentType", "datastore");
    resultsDict.put("observedUnit", "ms");
    resultsDict.put("observedValue", duration);
    resultsDict.put(STATUS_TEXT, passStatus.toLowerCase());
    resultsDict.put("targetValue", expectedDuration);
    resultsDict.put("time",  new Date().toInstant().toString());

    return resultsDict;
  }
}
