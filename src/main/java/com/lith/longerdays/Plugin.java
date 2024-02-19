package com.lith.longerdays;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
  private static final Logger LOGGER = Logger.getLogger("tpa");

  public void onEnable() {
    LOGGER.info("Longer days enabled");
  }

  public void onDisable() {
    LOGGER.info("Longer days disabled");
  }
}
