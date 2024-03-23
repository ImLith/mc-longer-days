package com.lith.longerdays;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.scheduler.BukkitRunnable;
import com.lith.lithcore.abstractClasses.MainPlugin;
import com.lith.longerdays.config.ConfigManager;
import com.lith.longerdays.event.player.PlayerBed;
import com.lith.longerdays.runnable.WorldTimeCycle;

public class Plugin extends MainPlugin<ConfigManager> {
  private ConfigManager cm;

  public void onEnable() {
    new ConfigManager(this);

    this.registerEvents();
    this.registerRunnables();

    Static.log.info("Plugin enabled");
  }

  public void onDisable() {
    this.setDaylightCycle(true);

    Static.log.info("Plugin disabled");
  }

  public ConfigManager getConfigManager() {
    return this.cm;
  }

  private void registerEvents() {
    this.getServer().getPluginManager().registerEvents(new PlayerBed(), this);
  }

  private void registerRunnables() {
    new BukkitRunnable() {
      @Override
      public void run() {
        setDaylightCycle(false);
        registerRunnable();
      }
    }.runTask(this);
  }

  private void setDaylightCycle(final boolean value) {
    Bukkit.getWorlds()
        .stream()
        .filter(world -> this.cm.getWorlds().contains(world.getName()))
        .forEach(world -> {
          world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, value);
          Static.log.info("Setting GameRule.DO_DAYLIGHT_CYCLE to " + value + " for world '" + world.getName() + "'");
        });
  }

  private void registerRunnable() {
    final WorldTimeCycle cycle = new WorldTimeCycle(this);

    Bukkit.getWorlds()
        .stream()
        .filter(world -> this.cm.getWorlds().contains(world.getName()))
        .forEach(cycle::runCycles);
  }
}
