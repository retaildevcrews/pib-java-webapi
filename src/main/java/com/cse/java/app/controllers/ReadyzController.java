package com.cse.java.app.controllers;

import com.cse.java.app.utils.CommonUtils;
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
  public Mono<ResponseEntity<String>> readinessCheck() {
    logger.info("readiness endpoint");
    return Mono.just(ResponseEntity.ok("ready"));
  }
}
