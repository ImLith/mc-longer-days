package com.lith.longerdays.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.lith.lithcore.abstractClasses.AbstractConfigManager;
import com.lith.longerdays.Plugin;
import com.lith.longerdays.Static;

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
        final int day = this.config.getInt("day", Static.defaultDayTime);

        if (day <= 0) {
            this.day = Static.defaultDayTime;

            Static.log.warning("Set day cycle to " + day + " minutes is not safe, reverting to default "
                    + Static.defaultDayTime + " minutes!");
        } else {
            this.day = day;

            Static.log.info("Set day cycle to " + this.day + " minutes.");
        }
    }

    private void validateNight() {
        final int night = this.config.getInt("night", Static.defaultNightTime);

        if (night <= 0) {
            this.night = Static.defaultNightTime;

            Static.log.warning("Set night cycle to " + night + " minutes is not safe, reverting to default "
                    + Static.defaultNightTime + " minutes!");
        } else {
            this.night = night;

            Static.log.info("Set night cycle to " + this.night + " minutes");
        }
    }

    private void validateWorld() {
        final List<String> worlds = this.config.getStringList("worlds");

        this.worlds = new HashSet<>();
        this.worlds.addAll(worlds);
        this.worlds = Collections.unmodifiableSet(this.worlds);
    }
}
