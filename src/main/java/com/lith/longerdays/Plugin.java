package com.lith.longerdays;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.scheduler.BukkitRunnable;

import com.lith.lithcore.abstractClasses.AbstractPlugin;
import com.lith.longerdays.config.ConfigManager;
import com.lith.longerdays.event.player.PlayerBed;
import com.lith.longerdays.runnable.WorldTimeCycle;

public class Plugin extends AbstractPlugin<Plugin, ConfigManager> {
  public static Plugin plugin;

  public void onEnable() {
    Plugin.plugin = this;

    new ConfigManager(this);

    this.registerEvents();
    this.registerRunnables();

    Static.log.info("Plugin enabled");
  }

  public void onDisable() {
    this.setDaylightCycle(true);

    Static.log.info("Plugin disabled");
  }

  @Override
  protected void registerEvents() {
    registerEvent(new PlayerBed());
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
        .filter(world -> this.configs.getWorlds().contains(world.getName()))
        .forEach(world -> {
          world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, value);
          Static.log.info("Setting GameRule.DO_DAYLIGHT_CYCLE to " + value + " for world '" + world.getName() + "'");
        });
  }

  private void registerRunnable() {
    final WorldTimeCycle cycle = new WorldTimeCycle();

    Bukkit.getWorlds()
        .stream()
        .filter(world -> this.configs.getWorlds().contains(world.getName()))
        .forEach(cycle::runCycles);
  }
}
