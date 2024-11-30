package net.kettlemc.lobby;

import net.kettlemc.lobby.command.BuildCommand;
import net.kettlemc.lobby.config.Configuration;
import net.kettlemc.lobby.config.Messages;
import net.kettlemc.lobby.listener.LobbyListener;
import net.kettlemc.lobby.loading.Loadable;
import net.kettlemc.kcommon.bukkit.ContentManager;
import net.kettlemc.kcommon.language.MessageManager;
import net.kettlemc.klanguage.api.LanguageAPI;
import net.kettlemc.klanguage.bukkit.BukkitLanguageAPI;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Lobby implements Loadable {

    public static final LanguageAPI<Player> LANGUAGE_API = BukkitLanguageAPI.of();
    private static Lobby instance;

    private final ContentManager contentManager;
    private final JavaPlugin plugin;
    private BukkitAudiences adventure;
    private MessageManager messageManager;

    public Lobby(JavaPlugin plugin) {
        this.plugin = plugin;
        this.contentManager = new ContentManager(plugin);
    }

    public static Lobby instance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;

        this.plugin.getLogger().info("Hello, World!");

        if (!Configuration.load()) {
            this.plugin.getLogger().severe("Failed to load config!");
        }

        if (!Messages.load()) {
            this.plugin.getLogger().severe("Failed to load messages!");
        }

        this.adventure = BukkitAudiences.create(this.plugin);
        this.messageManager = new MessageManager(Messages.PREFIX, LANGUAGE_API, this.adventure());

        this.plugin.getLogger().info("Registering listeners and commands...");
        this.contentManager.registerListener(new LobbyListener());
        this.contentManager.registerCommand("build", new BuildCommand());

    }

    @Override
    public void onDisable() {
        this.plugin.getLogger().info("Goodbye, World!");
        Configuration.unload();
    }

    public BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    public JavaPlugin plugin() {
        return plugin;
    }

    /**
     * Checks if the sender has permission to run the command.
     * When other is true, it checks for the permission to run the command on other players.
     * When other is false, it checks for the permission to run the command on themselves.
     * When the sender is a console, it always returns true.
     * <p>
     * When the sender has the permission to run the command on other players, they also have the permission to run the command on themselves.
     *
     * @param sender  The sender to check
     * @param command The command to check
     * @param other   Whether to check for the permission to run the command on other players
     * @return True if the sender has permission to run the command, false otherwise
     */
    public boolean checkPermission(CommandSender sender, Command command, boolean other) {
        return (sender instanceof ConsoleCommandSender)
                || (sender.hasPermission(Configuration.PERMISSION_LAYOUT_OTHER.getValue().replace("%command%", command.getLabel())))
                || (!other && sender.hasPermission(Configuration.PERMISSION_LAYOUT.getValue().replace("%command%", command.getLabel())));
    }


    public ContentManager contentManager() {
        return contentManager;
    }

    public MessageManager messageManager() {
        return messageManager;
    }
}
