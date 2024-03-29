// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See LICENSE in the project root for license information.

package com.pib.japp.controllers;

import com.pib.japp.config.BuildConfig;
import com.pib.japp.utils.CommonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@Tag(name = "Version")
public class VersionController {

  private static final Logger logger = LogManager.getLogger(VersionController.class);

  @Autowired
  ApplicationContext context;

  @Autowired
  CommonUtils commonUtils;

  @Autowired
  Environment environment;

  /**
   * Returns the application build version.
   *
   * @param response ServerHttpResponse passed into the alternate version handler by Spring
   * @return Mono{@literal <}Map{@literal <}String,
   *      String{@literal <}{@literal <} container the build number
  */
  @GetMapping(name = "Java-App Version Controller",
      value = "/version",
      produces = MediaType.TEXT_PLAIN_VALUE)
  @Operation(summary = "Returns the application version")
  @ApiResponse(responseCode = "200",
               content = @Content(schema = @Schema(type = "string")))
  public Mono<String> version(ServerHttpResponse response) {
    try {
      response.setStatusCode(HttpStatus.OK);
      String version = context.getBean(BuildConfig.class).getBuildVersion();

      return Mono.just(version);
    } catch (Exception ex) {
      logger.warn("Error received in VersionController", ex);
      return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "version Error"));
    }
  }
}
