// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See LICENSE in the project root for license information.

package com.pib.japp.config;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


@ConfigurationProperties(prefix = "")
@Validated
public class AppConfigProperties {
  @Valid
  @NotBlank
  @Getter @Setter
  private String region;

  @Valid
  @NotBlank
  @Getter @Setter
  private String zone;

  @Valid
  @Getter @Setter
  private String urlPrefix;
  
  @Valid
  @NotBlank
  @Getter @Setter
  private String urlPrefixValue;

  @Valid
  @NotBlank
  @Getter @Setter
  private String secretsVolume;

  @Valid
  @NotBlank
  @Pattern(regexp = "^TRACE$|^trace$|^DEBUG$|^debug$|^INFO$|^info$|^WARN$|^warn$|^ERROR$|^error$|^FATAL$|^fatal$|^OFF$|^off$",
          message = "Log levels must be any one of: TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF")
  @Getter @Setter
  private String requestLogLevel;

  // standard getters and setters
  @Override
  public String toString() {
    return String.format("Region: %s, Zone: %s, urlPrefix: %s, urlPrefixValue: %s", this.region, this.zone, this.urlPrefix, this.urlPrefixValue);
  }
}
