package com.cse.imdb.app.models;

import com.cse.imdb.app.config.BuildConfig;
import com.cse.imdb.app.config.NgsaConfigProperties;
import javax.validation.Valid;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(NgsaConfigProperties.class)
public class NgsaConfig {
  @Bean
  public BuildConfig buildConfig() {
    return new BuildConfig();
  }

  @Bean
  @Valid
  public NgsaConfigProperties ngsaConfigProperties() {
    return new NgsaConfigProperties();
  }
}
