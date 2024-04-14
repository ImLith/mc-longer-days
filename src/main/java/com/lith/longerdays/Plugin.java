package com.lith.longerdays;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import com.lith.lithcore.abstractClasses.AbstractPlugin;
import com.lith.lithcore.utils.WorldUtil;
import com.lith.longerdays.config.ConfigManager;
import com.lith.longerdays.event.PlayerBedInteractionEvent;
import com.lith.longerdays.runnable.WorldTimeCycle;

public class Plugin extends AbstractPlugin<Plugin, ConfigManager> {
  @Override
  public void onEnable() {
    configs = new ConfigManager(this);
    super.onEnable();
  }

  @Override
  public void onDisable() {
    WorldUtil.setDaylightCycle(true, this.configs.getWorlds());
    super.onDisable();
  }

  @Override
  protected void registerEvents() {
    registerEvent(new PlayerBedInteractionEvent());
  }

  @Override
  protected void registerRunnables() {
    new BukkitRunnable() {
      @Override
      public void run() {
        WorldUtil.setDaylightCycle(false, configs.getWorlds());
        registerWorldTimeCycle();
      }
    }.runTask(this);
  }

  private void registerWorldTimeCycle() {
    final WorldTimeCycle cycle = new WorldTimeCycle(this);

    Bukkit.getWorlds()
        .stream()
        .filter(world -> configs.getWorlds().contains(world.getName()))
        .forEach(cycle::runCycles);
  }
}
