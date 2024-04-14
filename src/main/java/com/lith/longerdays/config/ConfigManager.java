package com.lith.longerdays.config;

import java.util.HashSet;
import java.util.Set;
import com.lith.lithcore.abstractClasses.AbstractConfigManager;
import com.lith.longerdays.Plugin;
import com.lith.longerdays.Static;
import com.lith.longerdays.Static.Defaults;
import lombok.Getter;

public class ConfigManager extends AbstractConfigManager<Plugin, ConfigManager> {
    @Getter
    private int day;
    @Getter
    private int night;
    @Getter
    private Set<String> worlds;

    public ConfigManager(final Plugin plugin) {
        super(plugin);
    }

    @Override
    public void load() {
        this.day = validateTime(Static.ConfigKeys.DAY, Defaults.DAY_TIME);
        this.night = validateTime(Static.ConfigKeys.NIGHT, Defaults.NIGHT_TIME);

        this.worlds = new HashSet<>();
        this.worlds.addAll(this.config.getStringList("worlds"));
    }

    private int validateTime(String key, int defaultTime) {
        int time = config.getInt(key, defaultTime);

        if (time < 1) {
            time = defaultTime;
            plugin.log.warning("Set " + key + " cycle to " + time + " minutes is not safe, reverting to default!");
        }

        plugin.log.info("Set " + key + " cycle to " + time + " minutes.");
        return time;
    }
}
