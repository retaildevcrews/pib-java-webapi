package com.cse.imdb.app.services.configuration;

import com.cse.imdb.app.services.volumes.CosmosConfigs;

public interface IConfigurationService {

  CosmosConfigs getConfigEntries();
}
