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
    private final Plugin plugin;
    private final Map<String, Long> counts;

    public WorldTimeCycle(Plugin plugin) {
        this.counts = new HashMap<>();
        this.plugin = plugin;
    }

    public void runCycles(final World world) {
        new BukkitRunnable() {
            @Override
            public void run() {
                setTime(world, TimeUtil.convertMinsToTicks(
                        WorldUtil.isDay(world) ? plugin.configs.getDay() : plugin.configs.getNight()));
            }
        }.runTaskTimer(plugin, 0, 1);

        plugin.log.info("Running day and night cycles for world '" + world.getName() + "'");
    }

    private void setTime(final World world, final long time) {
        final String worldName = world.getName();
        final double ratio = (1.0 / (time / (double) WorldConstant.DAY_TIME_MAX_VALUE));
        long currentTime = world.getTime();

        counts.putIfAbsent(worldName, 0L);

        if (ratio > 1.0) {
            currentTime += Math.round(ratio);

            world.setTime(currentTime);
            counts.put(worldName, 0L);
        } else if (ratio < 1.0) {
            final long count = counts.get(worldName);
            long countSetter = count - 1;

            if (count <= 0) {
                currentTime += 1;

                world.setTime(currentTime);
                countSetter = Math.round(1.0 / ratio) - 1;
            }

            counts.put(worldName, countSetter);
        } else {
            world.setTime(++currentTime);
        }
    }
}
