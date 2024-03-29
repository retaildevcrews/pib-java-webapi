// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See LICENSE in the project root for license information.

package com.pib.japp.utils;

import com.pib.japp.config.BuildConfig;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.text.MessageFormat;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.stereotype.Component;

/**
 * CommonUtils.
 */
@Component
public class CommonUtils {

  @Autowired
  BuildConfig buildConfig;

  static Environment environment;

  private CommonUtils(Environment environment) {
    // disable constructor for utility class
    CommonUtils.environment = environment;
  }

  /**
   * handleCliOptions.
   *
   * @param args the log level in string form.
   */
  public static void handleCliOptions(String[] args) {
    if (args != null) {
      SimpleCommandLinePropertySource commandLinePropertySource =
          new SimpleCommandLinePropertySource(args);
      Arrays.stream(commandLinePropertySource.getPropertyNames()).forEach(s -> {
        if (s.equals("log-level") || s.equals("l")) {
          Level level = setLogLevel(commandLinePropertySource.getProperty(s));
          if (level == null) {
            printCmdLineHelp();
            System.exit(-1);
          }
          Configurator.setLevel(LogManager.getRootLogger().getName(), level);
        } else if (s.equals("help") || s.equals("h")) {
          printCmdLineHelp();
          System.exit(0);
        } else if (s.equals("version") || s.equals("v")) {
          System.exit(0);
        }
      });
    }
  }

  /**
   * prints the application version.
   */
  @SuppressWarnings("PMD.UnusedPrivateMethod") // Invoked when bean is initialized
  @PostConstruct
  private void printCmdLineVersion() {
    if (buildConfig != null) {
      String version = buildConfig.getBuildVersion();
      System.out.printf("\r\nApplication Version:\r\n \t%s \n\n", version);
    }
  }

  private static Level setLogLevel(String logLevel) {
    switch (logLevel) {
      case "trace":
        return Level.TRACE;
      case "debug":
        return Level.DEBUG;
      case "info":
        return Level.INFO;
      case "warn":
        return Level.WARN;
      case "error":
        return Level.ERROR;
      case "fatal":
        return Level.FATAL;
      default:
        return null;
    }
  }


  /**
   * validate cli dry run option.
   */
  public static void validateCliDryRunOption(ApplicationArguments applicationArguments,
                                              BuildConfig buildConfig) {
    if (applicationArguments != null) {
      SimpleCommandLinePropertySource commandLinePropertySource =
          new SimpleCommandLinePropertySource(applicationArguments.getSourceArgs());
      Arrays.stream(commandLinePropertySource.getPropertyNames()).forEach(s -> {
        if (s.equals("dry-run") || s.equals("d")) {
          printDryRunParameters(buildConfig);
          System.exit(0);
        }
      });
    }
  }

  @SuppressFBWarnings({"NP_UNWRITTEN_FIELD", "UWF_UNWRITTEN_FIELD"})
  @SuppressWarnings ("squid:S106") // System.out needed to print usage
  static void printDryRunParameters(BuildConfig buildConfig) {
    System.out.println(MessageFormat.format("Version                    {0}",
        buildConfig.getBuildVersion()));
  }

  /**
   * prints the command line help.
   */
  @SuppressWarnings ("squid:S106") // System.out needed to print usage
  public static void printCmdLineHelp() {
    System.out.println("\r\nUsage:\r\n"
        + "\tmvn clean spring-boot:run -Dspring-boot.run.arguments=[options] \r\n"
        + "\r\n"
        + "\texport [env var]=<value> \r\n"
        + "\tmvn clean spring-boot:run \r\n"
        + "\r\nOptions: \r\n"
        + "\t--help                                    \t\t Show help and usage information\r\n"
        + "\t--version                                 \t\t Show version information\r\n"
        + "\t--dry-run                                 \t\t Validates configuration\r\n"
        + "\t--log-level=<trace|info|warn|error|fatal> \t\t Log Level [default: Error]\r\n"
        + "\t--zone                                    "
        + "\t\t Zone for log (string) [default: dev]\r\n"
        + "\t--region                                  "
        + "\t\t Region for log (string) [default: dev]\r\n"
        + "\t--secrets-volume                          "
        + "\t\t Secrets Volume Path from project root directory (string) [default: secrets]\r\n"
        + "\r\nEnv vars: \r\n"
        + "\tZONE \r\n"
        + "\tREGION \r\n"
        + "\tSECRETS_VOLUME \r\n");
  }

  /**
   * Checks if a String is not null, not empty and not whitespace.
   */
  public static boolean isNullWhiteSpace(final String string) {
    return string == null || string.isEmpty() || string.trim().isEmpty();
  }
}
