// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See LICENSE in the project root for license information.

package com.pib.japp.services.volumes;

import com.pib.japp.Constants;
import com.pib.japp.error.AppException;
import com.pib.japp.utils.CommonUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class VolumeConfigService {

  private static final Logger logger = LogManager.getLogger(VolumeConfigService.class);

  @Autowired
  Environment environment;

  /**
   * Currently this function is not used anywhere.
   * Get all the secrets from a given volume.
   * @throws AppException if volume not found
   */
  public Map<String, String> getSecretsFromVolume() throws AppException {

    String volume = environment.getProperty(Constants.SECRETS_VOLUME_ARGUMENT);

    if (CommonUtils.isNullWhiteSpace(volume)) {
      throw new AppException(Constants.SECRETS_VOLUME_EMPTY);
    }

    Path filePath = Paths.get(volume);
    if (!Files.exists(filePath)) {
      throw new AppException(Constants.SECRETS_VOLUME_NOT_FOUND);
    }
    return getAllSecrets(volume);

  }

  /**
   * Get secret from file.
   * @throws AppException if secret key not found
   */
  public String getSecretFromFile(String key) throws AppException {

    String volume = environment.getProperty(Constants.SECRETS_VOLUME_ARGUMENT);
    String val = "";

    if (!CommonUtils.isNullWhiteSpace(volume)) {
      Path filePath = Paths.get(volume);

      if (!Files.exists(filePath)) {
        throw new AppException(Constants.SECRETS_VOLUME_NOT_FOUND);
      } else {
        filePath = Path.of(volume + "/" + key);
        if (!Files.exists(filePath)) {
          throw new AppException(Constants.SECRETS_NOT_FOUND);
        } else {
          try {
            return new String(Files.readAllBytes(filePath));
          } catch (IOException ex) {
            logger.error(ex.getMessage());
          }
        }
      }

    } else {
      throw new AppException(Constants.SECRETS_VOLUME_EMPTY);
    }
    return val;
  }

  /**
   * Get All secrets from volume.
   */
  public Map<String, String> getAllSecrets(String volume) {

    Map<String, String> secrets = new HashMap<>();
    File directoryPath = new File(volume);
    File[] filesList = directoryPath.listFiles();
    for (File file : filesList) {
      try {
        secrets.put(file.getName(), Files.readAllBytes(Path.of(file.getAbsolutePath())).toString());
      } catch (IOException ex) {
        logger.error(ex.getMessage());
      }
    }
    return secrets;
  }
}
