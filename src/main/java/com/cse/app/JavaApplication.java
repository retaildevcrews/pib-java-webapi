// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See LICENSE in the project root for license information.

package com.cse.app;

import com.cse.app.utils.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.config.EnableWebFlux;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableWebFlux
@EnableSwagger2
@ComponentScan("com.cse.app")
public class JavaApplication {
  private static final Logger logger =   LogManager.getLogger(JavaApplication.class);

  /**
   * main.
   */
  public static void main(String[] args) {

    try {
      SpringApplication.run(JavaApplication.class, args);
      CommonUtils.handleCliOptions(args);
    } catch (Exception ex) {
      logger.error(ex.getMessage());
      System.exit(1);
    }
  }
}
