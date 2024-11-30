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

import java.util.*;
import java.util.stream.Collectors;

public class BuildCommand implements CommandExecutor, TabCompleter {

    private static final Set<UUID> BUILDERS = new HashSet<>();


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

            if (BUILDERS.contains(target.getUniqueId())) {
                BUILDERS.remove(target.getUniqueId());
                Lobby.instance().messageManager().sendMessage(sender, Messages.BUILD_DISABLED_OTHER, Placeholder.of("target", (ctx, arg) -> target.getName()));
                Lobby.instance().messageManager().sendMessage(target, Messages.BUILD_DISABLED);
            } else {
                BUILDERS.add(target.getUniqueId());
                Lobby.instance().messageManager().sendMessage(sender, Messages.BUILD_ENABLED_OTHER, Placeholder.of("target", (ctx, arg) -> target.getName()));
                Lobby.instance().messageManager().sendMessage(target, Messages.BUILD_ENABLED);
            }
        } else {
            if (!(sender instanceof Player)) {
                Lobby.instance().messageManager().sendMessage(sender, Messages.PLAYER_ONLY);
                return true;
            }

            Player player = (Player) sender;

            if (BUILDERS.contains(player.getUniqueId())) {
                BUILDERS.remove(player.getUniqueId());
                Lobby.instance().messageManager().sendMessage(player, Messages.BUILD_DISABLED);
            } else {
                BUILDERS.add(player.getUniqueId());
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

    public static boolean isBuilder(Player player) {
        return BUILDERS.contains(player.getUniqueId());
    }
}
