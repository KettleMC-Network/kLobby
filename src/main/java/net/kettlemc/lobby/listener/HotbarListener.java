package net.kettlemc.lobby.listener;

import com.cryptomorin.xseries.XMaterial;
import net.kettlemc.kcommon.bukkit.itemstack.ItemBuilder;
import net.kettlemc.kcommon.language.AdventureUtil;
import net.kettlemc.klanguage.common.LanguageEntity;
import net.kettlemc.lobby.Lobby;
import net.kettlemc.lobby.config.Messages;
import net.kettlemc.lobby.data.LobbyPlayer;
import net.kettlemc.lobby.event.PlayerToggleBuildEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HotbarListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.getInventory().clear();
        setHotbar(player);
    }

    @EventHandler
    public void onBuildToggle(PlayerToggleBuildEvent event) {
        setHotbar(event.getPlayer());
    }

    //@EventHandler
    public void onLanguageChange(/*PlayerLanguageChangeEvent event*/) {
        // setHotbar(event.getPlayer());
    }


    public static void setHotbar(Player player) {
        player.getInventory().clear();
        LanguageEntity entity = Lobby.LANGUAGE_API.getEntity(player.getUniqueId());

        player.getInventory().setItem(0, ItemBuilder.of(XMaterial.COMPASS)
                .hideAttributes()
                .name(AdventureUtil.componentToLegacy(Messages.ITEM_TELEPORTER_NAME.value(entity)))
                .lore(AdventureUtil.componentToLegacy(Messages.ITEM_TELEPORTER_LORE.value(entity)))
                .build()
        );

        player.getInventory().setItem(7, ItemBuilder.of(XMaterial.FISHING_ROD)
                .hideAttributes()
                .name(AdventureUtil.componentToLegacy(Messages.ITEM_GRAPPLING_HOOK_NAME.value(entity)))
                .lore(AdventureUtil.componentToLegacy(Messages.ITEM_GRAPPLING_HOOK_LORE.value(entity)))
                .build()
        );

        updateVisibility(player);

    }

    public static void updateVisibility(Player player) {
        LanguageEntity entity = Lobby.LANGUAGE_API.getEntity(player.getUniqueId());
        if (Lobby.PLAYERS.get(player.getUniqueId()).visibility() == LobbyPlayer.Visibility.VISIBLE) {
            player.getInventory().setItem(8, ItemBuilder.of(XMaterial.LIME_DYE)
                    .hideAttributes()
                    .name(AdventureUtil.componentToLegacy(Messages.ITEM_VISIBILITY_VISIBLE_NAME.value(entity)))
                    .lore(AdventureUtil.componentToLegacy(Messages.ITEM_VISIBILITY_VISIBLE_LORE.value(entity)))
                    .build()
            );
        } else {
            player.getInventory().setItem(8, ItemBuilder.of(XMaterial.GRAY_DYE)
                    .hideAttributes()
                    .name(AdventureUtil.componentToLegacy(Messages.ITEM_VISIBILITY_HIDDEN_NAME.value(entity)))
                    .lore(AdventureUtil.componentToLegacy(Messages.ITEM_VISIBILITY_HIDDEN_LORE.value(entity)))
                    .build()
            );
        }
    }

}
