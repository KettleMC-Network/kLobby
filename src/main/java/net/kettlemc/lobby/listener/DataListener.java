package net.kettlemc.lobby.listener;

import net.kettlemc.lobby.Lobby;
import net.kettlemc.lobby.config.Configuration;
import net.kettlemc.lobby.data.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DataListener implements Listener {

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        try {
            LobbyPlayer player = Lobby.instance().dataHandler().load(event.getUniqueId().toString()).get(10, TimeUnit.SECONDS);
            Lobby.PLAYERS.put(event.getUniqueId(), player);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            event.setKickMessage("Failed to load player data!");
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!Lobby.PLAYERS.containsKey(player.getUniqueId())) {
            Lobby.instance().plugin().getLogger().warning("Player " + player.getName() + " joined without having any data!");
            return;
        }

        if (Configuration.LAST_LOCATION_ON_JOIN.getValue()) {
            player.teleport(Lobby.PLAYERS.get(player.getUniqueId()).getLocation());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (!Lobby.PLAYERS.containsKey(player.getUniqueId())) {
            Lobby.instance().plugin().getLogger().warning("Player " + player.getName() + " left without having any data!");
            return;
        }

        Lobby.PLAYERS.get(player.getUniqueId()).setLocation(player.getLocation());

        Bukkit.getScheduler().runTaskAsynchronously(Lobby.instance().plugin(), () -> {
            try {
                Lobby.instance().dataHandler().save(Lobby.PLAYERS.get(player.getUniqueId())).get(10, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
        });
    }

}
