package com.lith.longerdays.event;

import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import com.lith.lithcore.utils.WorldUtil;

public class PlayerBedInteractionEvent implements Listener {
    private int sleepingCount;
    private boolean skipNight;

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (!WorldUtil.isNight(event.getPlayer().getWorld()))
            return;
        if (event.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK)
            return;

        World world = event.getPlayer().getWorld();
        int worldPlayerCount = world.getPlayers().size();

        sleepingCount++;

        int percentage = world.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
        int sleepingPercentage = (int) (((double) sleepingCount / worldPlayerCount) * 100);

        if (sleepingPercentage >= percentage)
            skipNight = true;
    }

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        if (skipNight) {
            skipNight = false;
            sleepingCount = 0;

            event.getPlayer().getWorld().setTime(0);
        } else if (sleepingCount > 0)
            sleepingCount--;
    }
}
