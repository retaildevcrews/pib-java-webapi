package com.cse.app.controllers;

import com.cse.app.services.volumes.VolumeConfigService;
import com.cse.app.utils.InvalidParameterResponses;
import io.swagger.annotations.ApiParam;
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
public class SecretsController {

  private static final Logger logger = LogManager.getLogger(SecretsController.class);

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
  public Mono<ResponseEntity<String>> getSecret(
      @ApiParam(value = "Returns the Key Vault secret mounted in the secrets volume", example = "Database", required = true) @PathVariable("key") String key) {

    try {
      String secret = volumeConfigService.getSecretFromFile(key);
      return Mono.justOrEmpty(ResponseEntity.ok(secret));
    } catch (Exception ex) {
      logger.warn(MessageFormat.format("SecretsControllerException {0}", ex.getMessage()));
      logger.error("Invalid secret key");
      return Mono
          .just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("secret not found"));
    }
  }
}
