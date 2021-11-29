package shagejack.minecraftology.util;

import shagejack.minecraftology.handler.ConfigurationHandler;

public interface IConfigSubscriber {
    void onConfigChanged(ConfigurationHandler config);
}

