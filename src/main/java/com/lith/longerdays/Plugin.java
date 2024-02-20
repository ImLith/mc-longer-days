package com.lith.longerdays;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import com.lith.longerdays.config.ConfigManager;
import com.lith.longerdays.event.player.PlayerBed;
import com.lith.longerdays.runnable.WorldTimeCycle;

public class Plugin extends JavaPlugin {
  private ConfigManager cm;

  public void onEnable() {
    this.registerConfig();
    this.registerEvents();

    new BukkitRunnable() {
      @Override
      public void run() {
        setDaylightCycle(false);
        registerRunnables();
      }
    }.runTask(this);

    Static.log.info("Longer days enabled");
  }

  public void onDisable() {
    this.setDaylightCycle(true);

    Static.log.info("Longer days disabled");
  }

  public ConfigManager getConfigManager() {
    return this.cm;
  }

  private void registerConfig() {
    this.saveDefaultConfig();

    this.cm = new ConfigManager(this.getConfig());
  }

  private void registerEvents() {
    this.getServer().getPluginManager().registerEvents(new PlayerBed(), this);
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

  private void registerRunnables() {
    final WorldTimeCycle cycle = new WorldTimeCycle(this);

    Bukkit.getWorlds()
        .stream()
        .filter(world -> this.cm.getWorlds().contains(world.getName()))
        .forEach(cycle::runCycles);
  }
}
