package net.kettlemc.lobby.command;

import io.github.almightysatan.slams.Placeholder;
import net.kettlemc.kessentials.Essentials;
import net.kettlemc.lobby.Lobby;
import net.kettlemc.lobby.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BuildCommand implements CommandExecutor, TabCompleter {

    private static final String BUILD_META = "kettlemc.lobby.build";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!Essentials.instance().checkPermission(sender, command, false)) {
            Lobby.instance().messageManager().sendMessage(sender, Messages.NO_PERMISSION);
            return true;
        }


        if (args.length >= 1) {

            if (!Essentials.instance().checkPermission(sender, command, true)) {
                Lobby.instance().messageManager().sendMessage(sender, Messages.NO_PERMISSION);
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                Lobby.instance().messageManager().sendMessage(sender, Messages.PLAYER_NOT_FOUND);
                return true;
            }

            if (isBuilder(target)) {
                toggleBuild(target, false);
                Lobby.instance().messageManager().sendMessage(sender, Messages.BUILD_DISABLED_OTHER, Placeholder.of("target", (ctx, arg) -> target.getName()));
                Lobby.instance().messageManager().sendMessage(target, Messages.BUILD_DISABLED);
            } else {
                toggleBuild(target, true);
                Lobby.instance().messageManager().sendMessage(sender, Messages.BUILD_ENABLED_OTHER, Placeholder.of("target", (ctx, arg) -> target.getName()));
                Lobby.instance().messageManager().sendMessage(target, Messages.BUILD_ENABLED);
            }
        } else {
            if (!(sender instanceof Player)) {
                Lobby.instance().messageManager().sendMessage(sender, Messages.PLAYER_ONLY);
                return true;
            }

            Player player = (Player) sender;

            if (isBuilder(player)) {
                toggleBuild(player, false);
                Lobby.instance().messageManager().sendMessage(player, Messages.BUILD_DISABLED);
            } else {
                toggleBuild(player, true);
                Lobby.instance().messageManager().sendMessage(player, Messages.BUILD_ENABLED);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public void toggleBuild(Player player, boolean build) {
        if (build) {
            player.removeMetadata(BUILD_META, Lobby.instance().plugin());
        } else {
            player.setMetadata(BUILD_META, new FixedMetadataValue(Lobby.instance().plugin(), true));
        }
    }

    public static boolean isBuilder(Player player) {
        return player.hasMetadata(BUILD_META) && player.getMetadata(BUILD_META).get(0).asBoolean();
    }
}
