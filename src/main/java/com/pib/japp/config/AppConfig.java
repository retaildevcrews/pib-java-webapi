// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See LICENSE in the project root for license information.

package com.pib.japp.config;

import javax.validation.Valid;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AppConfigProperties.class)
public class AppConfig {
  @Bean
  public BuildConfig buildConfig() {
    return new BuildConfig();
  }

  @Bean
  @Valid
  public AppConfigProperties appConfigProperties() {
    return new AppConfigProperties();
  }
}
