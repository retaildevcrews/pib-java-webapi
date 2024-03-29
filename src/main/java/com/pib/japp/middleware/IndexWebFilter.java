// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See LICENSE in the project root for license information.

package com.pib.japp.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class IndexWebFilter implements WebFilter {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String uriPath = exchange.getRequest().getURI().getPath();
    if (uriPath.equals("/java-app") || uriPath.equals("/java-app/")) {
      return chain.filter(exchange.mutate().request(exchange.getRequest().mutate().path("/java-app/index.html").build()).build());
    }

    return chain.filter(exchange);
  }
}
