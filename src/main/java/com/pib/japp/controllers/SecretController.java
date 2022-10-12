// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See LICENSE in the project root for license information.

package com.pib.japp.controllers;

import com.pib.japp.services.volumes.VolumeConfigService;
import com.pib.japp.utils.InvalidParameterResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.text.MessageFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/secret")
@Tag(name = "Secret")
public class SecretController {

  private static final Logger logger = LogManager.getLogger(SecretController.class);

  @Autowired
  private VolumeConfigService volumeConfigService;

  @Autowired
  InvalidParameterResponses invalidParameterResponses;

  /**
   * Get Secret from volume.
   *
   * @return secret value
   */
  @GetMapping(value = "/{key}")
  @Operation(summary = "Returns the Key Vault secret mounted in the secrets volume")
  public Mono<ResponseEntity<String>> getSecret(
      @PathVariable("key") String key) {

    try {
      String secret = volumeConfigService.getSecretFromFile(key);
      return Mono.justOrEmpty(ResponseEntity.ok(secret));
    } catch (Exception ex) {
      logger.warn(MessageFormat.format("SecretControllerException {0}", ex.getMessage()));
      logger.error("Invalid secret key");
      return Mono
          .just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("secret not found"));
    }
  }
}
