package com.lith.longerdays.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.lith.lithcore.abstractClasses.AbstractConfigManager;
import com.lith.longerdays.Plugin;
import com.lith.longerdays.Static.Defaults;

public class ConfigManager extends AbstractConfigManager<Plugin, ConfigManager> {
    private int day;
    private int night;
    private Set<String> worlds;

    public ConfigManager(final Plugin plugin) {
        super(plugin);
    }

    @Override
    public void load() {
        this.validate();
    }

    public void validate() {
        this.validateDay();
        this.validateNight();
        this.validateWorld();
    }

    public int getDay() {
        return this.day;
    }

    public int getNight() {
        return this.night;
    }

    public Set<String> getWorlds() {
        return Collections.unmodifiableSet(this.worlds);
    }

    private void validateDay() {
        final int day = this.config.getInt("day", Defaults.DAY_TIME);

        if (day <= 0) {
            this.day = Defaults.DAY_TIME;

            Plugin.plugin.log.warning("Set day cycle to " + day + " minutes is not safe, reverting to default "
                    + Defaults.DAY_TIME + " minutes!");
        } else {
            this.day = day;

            Plugin.plugin.log.info("Set day cycle to " + this.day + " minutes.");
        }
    }

    private void validateNight() {
        final int night = this.config.getInt("night", Defaults.NIGHT_TIME);

        if (night <= 0) {
            this.night = Defaults.NIGHT_TIME;

            Plugin.plugin.log.warning("Set night cycle to " + night + " minutes is not safe, reverting to default "
                    + Defaults.NIGHT_TIME + " minutes!");
        } else {
            this.night = night;

            Plugin.plugin.log.info("Set night cycle to " + this.night + " minutes");
        }
    }

    private void validateWorld() {
        final List<String> worlds = this.config.getStringList("worlds");

        this.worlds = new HashSet<>();
        this.worlds.addAll(worlds);
        this.worlds = Collections.unmodifiableSet(this.worlds);
    }
}
