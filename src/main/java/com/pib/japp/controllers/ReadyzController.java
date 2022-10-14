// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See LICENSE in the project root for license information.

package com.pib.japp.controllers;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/readyz")
@Tag(name = "Readyz")
public class ReadyzController {

  private static final Logger logger = LogManager.getLogger(ReadyzController.class);

  @Autowired
  ApplicationContext context;

  @Autowired
  CommonUtils commonUtils;

  @Autowired
  Environment environment;

  /**
   * readinessCheck.
   *
   * @return application readiness
   */
  @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
  @Operation(summary = "Returns a plain text ready status")
  @ApiResponse(responseCode = "200", description = "Success",
               content = @Content(schema = @Schema(type = "string")))
  public Mono<ResponseEntity<String>> readinessCheck() {
    logger.info("readiness endpoint");
    return Mono.just(ResponseEntity.ok("ready"));
  }
}
