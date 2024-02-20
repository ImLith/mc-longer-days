package com.lith.longerdays.runnable;

import com.lith.longerdays.Plugin;
import com.lith.longerdays.Static;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import com.lith.longerdays.config.ConfigManager;
import main.java.com.lith.lithcore.utils.WorldUtil;
import main.java.com.lith.lithcore.constants.WorldConstant;
import main.java.com.lith.lithcore.utils.ConverterUtil;

public class WorldTimeCycle {
    private final Plugin plugin;
    private final ConfigManager cm;
    private final Map<String, Long> counts;

    public WorldTimeCycle(final Plugin plugin) {
        this.plugin = plugin;
        this.cm = this.plugin.getConfigManager();
        this.counts = new HashMap<>();
    }

    public void runCycles(final World world) {
        new BukkitRunnable() {
            @Override
            public void run() {
                final long worldTime = world.getTime();
                Integer configTime = null;

                if (WorldUtil.isDay(world))
                    configTime = cm.getDay();
                else if (WorldUtil.isNight(world))
                    configTime = cm.getNight();

                if (configTime != null)
                    setTime(world, ConverterUtil.convertMinsToTicks(configTime));
                else
                    Static.log.warning(world.getName() + " world time " + worldTime + " is impossible");
            }
        }.runTaskTimer(this.plugin, 0, 1);

        Static.log.info("Running day and night cycles for world '" + world.getName() + "'");
    }

    private void setTime(final World world, final long time) {
        final String worldName = world.getName();
        final double ratio = (1.0 / (time / (double) WorldConstant.DAY_TIME_MAX_VALUE));
        long currentTime = world.getTime();

        this.counts.putIfAbsent(worldName, 0L);

        if (ratio > 1.0) {
            currentTime += Math.round(ratio);

            world.setTime(currentTime);
            this.counts.put(worldName, 0L);
        } else if (ratio < 1.0) {
            final long count = this.counts.get(worldName);

            if (count <= 0) {
                currentTime += 1;

                world.setTime(currentTime);
                this.counts.put(worldName, Math.round(1.0 / ratio) - 1);
            } else {
                this.counts.put(worldName, count - 1);
            }
        } else {
            world.setTime(++currentTime);
        }
    }
}
