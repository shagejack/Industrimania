package shagejack.shagecraft.util;

import shagejack.shagecraft.handler.ConfigurationHandler;

public interface IConfigSubscriber {
    void onConfigChanged(ConfigurationHandler config);
}

