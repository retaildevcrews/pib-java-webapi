package com.cse.java.app.services.configuration;

import com.cse.java.app.services.volumes.CosmosConfigs;

public interface IConfigurationService {

  CosmosConfigs getConfigEntries();
}
