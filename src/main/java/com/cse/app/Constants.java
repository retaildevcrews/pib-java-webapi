// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See LICENSE in the project root for license information.

package com.cse.app;

/**
 * Constants.
 */
public final class Constants {
  public static final Integer MAX_BENCH_STR_SIZE = 1024 * 1024;

  public static final String WEB_INSTANCE_ROLE = "WEBSITE_ROLE_INSTANCE_ID";
  public static final String WEB_INSTANCE_ROLE_ID = "WEBSITE_ROLE_INSTANCE_ID";


  public static final String SECRETS_VOLUME_ARGUMENT = "secrets-volume";
  public static final String SECRETS_VOLUME_NOT_FOUND = "Volume does not exist.";
  public static final String SECRETS_NOT_FOUND = "Secret not found.";
  public static final String SECRETS_VOLUME_EMPTY = "Volume is empty.";


 
  private Constants() {
    // private constructor to hide public constructor
  }
}
