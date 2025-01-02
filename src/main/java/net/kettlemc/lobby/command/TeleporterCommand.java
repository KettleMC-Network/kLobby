package net.kettlemc.lobby.command;

import com.cryptomorin.xseries.XMaterial;
import net.kettlemc.kcommon.java.NumberUtil;
import net.kettlemc.kessentials.Essentials;
import net.kettlemc.lobby.Lobby;
import net.kettlemc.lobby.config.Messages;
import net.kettlemc.lobby.data.LobbyWarp;
import net.kettlemc.lobby.util.LobbyUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TeleporterCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!Essentials.instance().checkPermission(sender, command, false)) {
            Lobby.instance().messageManager().sendMessage(sender, Messages.NO_PERMISSION);
            return true;
        }

        if (!(sender instanceof Player)) {
            Lobby.instance().messageManager().sendMessage(sender, Messages.PLAYER_ONLY);
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            Lobby.instance().messageManager().sendMessage(sender, Messages.TELEPORTER_USAGE);
            return true;
        }

        if (args[0].equalsIgnoreCase("add")) {
            String[] parsedArgs = LobbyUtil.parseQuotationMarkArguments(args);

            if (parsedArgs.length != 6) {
                Lobby.instance().messageManager().sendMessage(sender, Messages.TELEPORTER_USAGE);
                return true;
            }

            ItemStack itemInHand = player.getItemInHand();

            if (itemInHand.getType() == XMaterial.AIR.parseMaterial()) {
                Lobby.instance().messageManager().sendMessage(sender, Messages.TELEPORTER_INVALID_ITEM);
                return true;
            }

            String name = parsedArgs[1];
            String version = parsedArgs[2];
            String modpack = parsedArgs[3];
            String warp = parsedArgs[4];

            if (!NumberUtil.isInteger(parsedArgs[5])) {
                Lobby.instance().messageManager().sendMessage(sender, Messages.TELEPORTER_USAGE);
                return true;
            }

            int slot = Integer.parseInt(parsedArgs[5]);

            if (LobbyWarp.WARPS.containsKey(slot)) {
                Lobby.instance().messageManager().sendMessage(sender, Messages.TELEPORTER_ALREADY_EXISTS);
                return true;
            }

            LobbyWarp lobbyWarp = new LobbyWarp(name, version, modpack, slot, warp, itemInHand);

            try {
                Lobby.instance().lobbyWarpDataHandler().save(lobbyWarp).get(10, TimeUnit.SECONDS);
                LobbyWarp.WARPS.put(slot, lobbyWarp);
            } catch (InterruptedException | TimeoutException | ExecutionException e) {
                Lobby.instance().messageManager().sendMessage(sender, Messages.TELEPORTER_COULD_NOT_ADD);
            }
        }


        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
