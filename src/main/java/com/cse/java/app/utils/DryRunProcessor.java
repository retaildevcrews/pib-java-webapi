package com.cse.java.app.utils;

import com.cse.java.app.config.BuildConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * DryRunProcessor.
 */
@Component
public class DryRunProcessor {

  @Autowired
  ApplicationArguments applicationArguments;

  @Autowired
  private BuildConfig buildConfig;


  private static final Logger logger = LogManager.getLogger(DryRunProcessor.class);

  /**
   * onApplicationEvent.
   */
  @EventListener
  public void onApplicationEvent(ContextRefreshedEvent event) {
    logger.info("Application Context has been fully started up");
    logger.info("All beans are now instantiated and ready to go!");
    CommonUtils.validateCliDryRunOption(applicationArguments, buildConfig);
  }

}
