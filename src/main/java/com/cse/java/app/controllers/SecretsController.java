package com.cse.java.app.controllers;

import com.cse.java.app.services.volumes.VolumeConfigService;
import com.cse.java.app.utils.InvalidParameterResponses;
import com.cse.java.app.utils.ParameterValidator;
import io.swagger.annotations.ApiParam;
import java.text.MessageFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/secret")
public class SecretsController {

  private static final Logger logger = LogManager.getLogger(SecretsController.class);

  @Autowired
  private ParameterValidator parameterValidator;

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
    if (Boolean.TRUE == parameterValidator.isValidSecretKey(key)) {

      try {
        String secret = volumeConfigService.getSecretFromFile(key);
        return Mono.justOrEmpty(ResponseEntity.ok(secret));
      } catch (Exception ex) {
        logger.warn(MessageFormat.format("ActorControllerException {0}", ex.getMessage()));
        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
      }

    } else {
      String invalidResponse = invalidParameterResponses.invalidSecretKey();
      logger.error("Key cannot be empty");

      return Mono
          .just(ResponseEntity.badRequest().contentType(MediaType.APPLICATION_PROBLEM_JSON).body(invalidResponse));
    }

  }
}
