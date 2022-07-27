package com.cse.app.config;

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
