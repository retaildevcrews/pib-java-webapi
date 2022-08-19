// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See LICENSE in the project root for license information.

package com.pib.japp.config;

import com.pib.japp.utils.UrlPrefixTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebfluxConfig implements WebFluxConfigurer {

  /**
   * This transformer will replace all {{url-prefix}} token in index.html and swagger.json
   */
  @Autowired
  UrlPrefixTransformer transformer;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
    registry.addResourceHandler("/index.html**")
        .addResourceLocations("classpath:/static/")
        .resourceChain(false)
        .addTransformer(transformer); // Adding urlPrefix transformer
    registry.addResourceHandler("/swagger.json**")
        .addResourceLocations("classpath:/static/")
        .resourceChain(false)
        .addTransformer(transformer); // Adding urlPrefix transformer

  }
}
