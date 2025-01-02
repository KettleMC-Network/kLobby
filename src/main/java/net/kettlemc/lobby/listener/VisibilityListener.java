package net.kettlemc.lobby.listener;

import com.cryptomorin.xseries.XMaterial;
import net.kettlemc.kcommon.language.AdventureUtil;
import net.kettlemc.kessentials.command.VanishCommand;
import net.kettlemc.klanguage.common.LanguageEntity;
import net.kettlemc.lobby.Lobby;
import net.kettlemc.lobby.config.Messages;
import net.kettlemc.lobby.data.LobbyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class VisibilityListener implements Listener {

    @EventHandler
    public void onVisibilityClick(PlayerInteractEvent event) {
        if (event.getItem().getType() != XMaterial.LIME_DYE.parseMaterial() || event.getItem().getType() != XMaterial.GRAY_DYE.parseMaterial()) {
            return;
        }

        Player player = event.getPlayer();
        LanguageEntity entity = Lobby.LANGUAGE_API.getEntity(player.getUniqueId());
        LobbyPlayer lobbyPlayer = Lobby.PLAYERS.get(player.getUniqueId());

        if (event.getItem().getItemMeta().getDisplayName().equals(AdventureUtil.componentToLegacy(Messages.ITEM_VISIBILITY_HIDDEN_NAME.value(entity)))) {
            lobbyPlayer.visibility(LobbyPlayer.Visibility.VISIBLE);
            for (Player online : player.getServer().getOnlinePlayers()) {
                player.hidePlayer(online);
            }
        } else if (event.getItem().getItemMeta().getDisplayName().equals(AdventureUtil.componentToLegacy(Messages.ITEM_VISIBILITY_VISIBLE_NAME.value(entity)))) {
            lobbyPlayer.visibility(LobbyPlayer.Visibility.HIDDEN);
            for (Player online : player.getServer().getOnlinePlayers()) {
                if (VanishCommand.isVanished(online)) {
                    continue;
                }
                player.showPlayer(online);
            }
        }

        HotbarListener.updateVisibility(player);
    }

}
