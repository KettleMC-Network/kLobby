package net.kettlemc.lobby.config;

import io.github.almightysatan.jaskl.Config;
import io.github.almightysatan.jaskl.entries.BooleanConfigEntry;
import io.github.almightysatan.jaskl.entries.IntegerConfigEntry;
import io.github.almightysatan.jaskl.entries.StringConfigEntry;
import io.github.almightysatan.jaskl.hocon.HoconConfig;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Configuration {

    public static final Path CONFIG_DIRECTORY = Paths.get("plugins", "kLobby");
    private static final Config CONFIG = HoconConfig.of(CONFIG_DIRECTORY.resolve("config.conf").toFile(), "Config for kLobby");
    private static final Config SQL_CONFIG = HoconConfig.of(CONFIG_DIRECTORY.resolve("sql.conf").toFile(), "Config for the sql connection");

    public static final StringConfigEntry SQL_HOST = StringConfigEntry.of(SQL_CONFIG, "sql.host", "The sql host", "localhost");
    public static final IntegerConfigEntry SQL_PORT = IntegerConfigEntry.of(SQL_CONFIG, "sql.port", "The sql port", 3306);
    public static final StringConfigEntry SQL_DATABASE = StringConfigEntry.of(SQL_CONFIG, "sql.database", "The sql database", "lobby");
    public static final StringConfigEntry SQL_USERNAME = StringConfigEntry.of(SQL_CONFIG, "sql.username", "The sql username", "root");
    public static final StringConfigEntry SQL_PASSWORD = StringConfigEntry.of(SQL_CONFIG, "sql.password", "The sql password", "password");

    public static final BooleanConfigEntry LAST_LOCATION_ON_JOIN = BooleanConfigEntry.of(CONFIG, "settings.join.teleport-to-last-location", "Whether to teleport the player to their last location on join", true);

    public static final StringConfigEntry PERMISSION_LAYOUT = StringConfigEntry.of(CONFIG, "settings.permission.layout", "The default permission layout", "system.lobby.%s");
    public static final StringConfigEntry PERMISSION_LAYOUT_OTHER = StringConfigEntry.of(CONFIG, "settings.permission.layout-other", "The default permission layout for others", "system.lobby.%s.other");

    private Configuration() {
    }

    public static boolean load() {
        try {
            CONFIG.load();
            CONFIG.write();

            SQL_CONFIG.load();
            SQL_CONFIG.write();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void unload() {
        CONFIG.close();
        SQL_CONFIG.close();
    }

    public static boolean write() {
        try {
            CONFIG.write();
            SQL_CONFIG.write();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
