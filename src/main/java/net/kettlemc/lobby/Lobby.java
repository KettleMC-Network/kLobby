package net.kettlemc.lobby;

import net.kettlemc.kcommon.bukkit.ContentManager;
import net.kettlemc.kcommon.data.DataHandler;
import net.kettlemc.kcommon.data.HibernateDataHandler;
import net.kettlemc.kcommon.language.MessageManager;
import net.kettlemc.klanguage.api.LanguageAPI;
import net.kettlemc.klanguage.bukkit.BukkitLanguageAPI;
import net.kettlemc.lobby.command.BuildCommand;
import net.kettlemc.lobby.config.Configuration;
import net.kettlemc.lobby.config.Messages;
import net.kettlemc.lobby.data.LobbyPlayer;
import net.kettlemc.lobby.listener.BuildListener;
import net.kettlemc.lobby.listener.DataListener;
import net.kettlemc.lobby.loading.Loadable;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class Lobby implements Loadable {

    public static final LanguageAPI<Player> LANGUAGE_API = BukkitLanguageAPI.of();
    private static Lobby instance;

    private final ContentManager contentManager;
    private final JavaPlugin plugin;
    private BukkitAudiences adventure;
    private MessageManager messageManager;

    private HibernateDataHandler<LobbyPlayer> dataHandler;

    public static final ConcurrentMap<UUID, LobbyPlayer> PLAYERS = new ConcurrentHashMap<>();

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

        this.dataHandler = new HibernateDataHandler<>(LobbyPlayer.class,
                (id) -> new LobbyPlayer(id, Bukkit.getWorlds().get(0).getSpawnLocation()),
                Configuration.SQL_HOST.getValue(),
                String.valueOf(Configuration.SQL_PORT.getValue()),
                Configuration.SQL_DATABASE.getValue(),
                Configuration.SQL_USERNAME.getValue(),
                Configuration.SQL_PASSWORD.getValue()
        );

        if (!this.dataHandler.initialize()) {
            this.plugin.getLogger().severe("Failed to connect to the database!");
        }

        this.adventure = BukkitAudiences.create(this.plugin);
        this.messageManager = new MessageManager(Messages.PREFIX, LANGUAGE_API, this.adventure());

        this.plugin.getLogger().info("Registering listeners and commands...");
        this.contentManager.registerListener(new BuildListener());
        this.contentManager.registerListener(new DataListener());
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


    public ContentManager contentManager() {
        return contentManager;
    }

    public MessageManager messageManager() {
        return messageManager;
    }

    public DataHandler<LobbyPlayer> dataHandler() {
        return dataHandler;
    }
}
