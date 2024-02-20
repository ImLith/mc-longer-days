package com.lith.longerdays.event.player;

import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import main.java.com.lith.lithcore.utils.WorldUtil;

public class PlayerBed implements Listener {
    private int sleeping;
    private boolean skipNight = false;

    @EventHandler
    public void onPlayerBedEnter(final PlayerBedEnterEvent event) {
        final World world = event.getPlayer().getWorld();
        int worldPlayerCount = world.getPlayers().size();

        if (WorldUtil.isNight(world)) {
            if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
                this.sleeping++;

                final int percentage = world.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
                Double sleepingPercentage = ((double) this.sleeping / worldPlayerCount) * 100;

                if (sleepingPercentage >= percentage)
                    this.skipNight = true;
            }
        }
    }

    @EventHandler
    public void onPlayerBedLeave(final PlayerBedLeaveEvent event) {
        final World world = event.getPlayer().getWorld();

        if (this.skipNight == true) {
            this.skipNight = false;
            this.sleeping = 0;
            world.setTime(0);
        } else if (this.sleeping > 0)
            this.sleeping--;
    }
}
