package net.kettlemc.lobby.listener;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import net.kettlemc.kcommon.language.AdventureUtil;
import net.kettlemc.klanguage.common.LanguageEntity;
import net.kettlemc.lobby.Lobby;
import net.kettlemc.lobby.config.Messages;
import net.kettlemc.lobby.data.LobbyWarp;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class TeleporterListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getItem().getType() != XMaterial.COMPASS.parseMaterial()) {
            return;
        }

        Player player = event.getPlayer();
        LanguageEntity entity = Lobby.LANGUAGE_API.getEntity(player.getUniqueId());
        if (!event.getItem().getItemMeta().getDisplayName().equals(AdventureUtil.componentToLegacy(Messages.ITEM_TELEPORTER_NAME.value(entity)))) {
            return;
        }

        openTeleporter(player);
    }

    private void openTeleporter(Player player) {
        Component component = Messages.MENU_TELEPORTER_TITLE.value(Lobby.LANGUAGE_API.getEntity(player.getUniqueId()));
        String title = AdventureUtil.componentToLegacy(component);
        Inventory inventory = Bukkit.createInventory(player, 9 * 6, title);
        player.openInventory(inventory);

        List<LobbyWarp> warps = LobbyWarp.WARPS.values().stream().sorted((w1, w2) -> w1.slot() - w2.slot()).collect(Collectors.toList());

        new BukkitRunnable() {
            int index = 0;

            @Override
            public void run() {

                if (player.getOpenInventory() == inventory && index < warps.size()) {
                    LobbyWarp warp = warps.get(index);
                    inventory.setItem(warp.slot(), warp.itemStack());
                    player.playSound(player.getLocation(), XSound.ENTITY_PLAYER_SMALL_FALL.parseSound(), 0.4F, 2.0F);
                    index++;
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(Lobby.instance().plugin(), 0, 2);
    }

}
