package com.cse.java.app.services.volumes;

public interface IVolumeCosmosConfigService {
  CosmosConfigs getAllCosmosConfigsFromVolume(String volume);

  String getCosmosConfigFromFile(String volume, String key);

}
