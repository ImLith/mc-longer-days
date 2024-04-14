package com.lith.longerdays.runnable;

import com.lith.longerdays.Plugin;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import com.lith.lithcore.utils.WorldUtil;
import com.lith.lithcore.constants.WorldConstant;
import com.lith.lithcore.utils.TimeUtil;

public class WorldTimeCycle {
    private final Map<String, Long> counts;

    public WorldTimeCycle() {
        this.counts = new HashMap<>();
    }

    public void runCycles(final World world) {
        new BukkitRunnable() {
            @Override
            public void run() {
                final long worldTime = world.getTime();
                Integer configTime = null;

                if (WorldUtil.isDay(world))
                    configTime = Plugin.plugin.configs.getDay();
                else if (WorldUtil.isNight(world))
                    configTime = Plugin.plugin.configs.getNight();

                if (configTime != null)
                    setTime(world, TimeUtil.convertMinsToTicks(configTime));
                else
                    Plugin.plugin.log.warning(world.getName() + " world time " + worldTime + " is impossible");
            }
        }.runTaskTimer(Plugin.plugin, 0, 1);

        Plugin.plugin.log.info("Running day and night cycles for world '" + world.getName() + "'");
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
