package net.kettlemc.lobby.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerToggleBuildEvent extends PlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final boolean build;

    public PlayerToggleBuildEvent(Player player, boolean build) {
        super(player);
        this.build = build;
    }

    public boolean build() {
        return build;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
