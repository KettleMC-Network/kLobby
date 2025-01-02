package net.kettlemc.lobby.config;

import io.github.almightysatan.slams.Slams;
import io.github.almightysatan.slams.minimessage.AdventureMessage;
import io.github.almightysatan.slams.parser.JacksonParser;
import net.kettlemc.kcommon.java.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Messages {

    private static final String DEFAULT_LANGUAGE = "de";

    private static final Path LANGUAGE_PATH = Paths.get("plugins", "kLobby", "languages");
    private static final Slams LANGUAGE_MANAGER = Slams.create(DEFAULT_LANGUAGE);

    public static final AdventureMessage PREFIX = AdventureMessage.of("miscellaneous.prefix", LANGUAGE_MANAGER);
    public static final AdventureMessage NO_PERMISSION = AdventureMessage.of("miscellaneous.no-permission", LANGUAGE_MANAGER);
    public static final AdventureMessage PLAYER_ONLY = AdventureMessage.of("miscellaneous.player-only", LANGUAGE_MANAGER);
    public static final AdventureMessage PLAYER_NOT_FOUND = AdventureMessage.of("miscellaneous.player-not-found", LANGUAGE_MANAGER);

    public static final AdventureMessage BUILD_ENABLED = AdventureMessage.of("command.build.enabled", LANGUAGE_MANAGER);
    public static final AdventureMessage BUILD_ENABLED_OTHER = AdventureMessage.of("command.build.enabled-other", LANGUAGE_MANAGER);
    public static final AdventureMessage BUILD_DISABLED = AdventureMessage.of("command.build.disabled", LANGUAGE_MANAGER);
    public static final AdventureMessage BUILD_DISABLED_OTHER = AdventureMessage.of("command.build.disabled-other", LANGUAGE_MANAGER);

    // Teleporter Command

    public static final AdventureMessage TELEPORTER_USAGE = AdventureMessage.of("command.teleporter.usage", LANGUAGE_MANAGER);

    public static final AdventureMessage TELEPORTER_ADD_USAGE = AdventureMessage.of("command.teleporter.add.usage", LANGUAGE_MANAGER);
    public static final AdventureMessage TELEPORTER_ADDED = AdventureMessage.of("command.teleporter.add.added", LANGUAGE_MANAGER);
    public static final AdventureMessage TELEPORTER_INVALID_ITEM = AdventureMessage.of("command.teleporter.add.invalid-item", LANGUAGE_MANAGER);
    public static final AdventureMessage TELEPORTER_ALREADY_EXISTS = AdventureMessage.of("command.teleporter.add.already-exists", LANGUAGE_MANAGER);
    public static final AdventureMessage TELEPORTER_COULD_NOT_ADD = AdventureMessage.of("command.teleporter.add.could-not-add", LANGUAGE_MANAGER);

    public static final AdventureMessage TELEPORTER_REMOVED = AdventureMessage.of("command.teleporter.remove.removed", LANGUAGE_MANAGER);
    public static final AdventureMessage TELEPORTER_REMOVE_USAGE = AdventureMessage.of("command.teleporter.remove.usage", LANGUAGE_MANAGER);
    public static final AdventureMessage TELEPORTER_DOES_NOT_EXIST = AdventureMessage.of("command.teleporter.remove.does-not-exist", LANGUAGE_MANAGER);
    public static final AdventureMessage TELEPORTER_COULD_NOT_REMOVE = AdventureMessage.of("command.teleporter.remove.could-not-remove", LANGUAGE_MANAGER);

    // Items

    public static final AdventureMessage ITEM_TELEPORTER_NAME = AdventureMessage.of("item.teleporter.name", LANGUAGE_MANAGER);
    public static final AdventureMessage ITEM_TELEPORTER_LORE = AdventureMessage.of("item.teleporter.lore", LANGUAGE_MANAGER);
    public static final AdventureMessage ITEM_GRAPPLING_HOOK_NAME = AdventureMessage.of("item.grappling-hook.name", LANGUAGE_MANAGER);
    public static final AdventureMessage ITEM_GRAPPLING_HOOK_LORE = AdventureMessage.of("item.grappling-hook.lore", LANGUAGE_MANAGER);
    public static final AdventureMessage ITEM_VISIBILITY_VISIBLE_NAME = AdventureMessage.of("item.visibility.visible.name", LANGUAGE_MANAGER);
    public static final AdventureMessage ITEM_VISIBILITY_VISIBLE_LORE = AdventureMessage.of("item.visibility.visible.lore", LANGUAGE_MANAGER);
    public static final AdventureMessage ITEM_VISIBILITY_HIDDEN_NAME = AdventureMessage.of("item.visibility.hidden.name", LANGUAGE_MANAGER);
    public static final AdventureMessage ITEM_VISIBILITY_HIDDEN_LORE = AdventureMessage.of("item.visibility.hidden.lore", LANGUAGE_MANAGER);

    // Menus

    public static final AdventureMessage MENU_TELEPORTER_TITLE = AdventureMessage.of("menu.teleporter.title", LANGUAGE_MANAGER);


    private Messages() {
    }

    /**
     * Loads all messages from the language files.
     *
     * @return Whether the loading was successful.
     */
    public static boolean load() {
        if (!LANGUAGE_PATH.toFile().exists())
            LANGUAGE_PATH.toFile().mkdirs();

        FileUtil.saveResourceAsFile(Messages.class, "lang/de.json", LANGUAGE_PATH.resolve("de.json"));
        FileUtil.saveResourceAsFile(Messages.class, "lang/en.json", LANGUAGE_PATH.resolve("en.json"));

        try {
            loadFromFilesInDirectory(LANGUAGE_PATH.toFile());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Loads all json files in the given directory.
     *
     * @param directory The directory to load from.
     * @throws IOException If an error occurs while loading.
     */
    private static void loadFromFilesInDirectory(@NotNull File directory) throws IOException {
        if (!directory.isDirectory()) return;
        for (File file : Objects.requireNonNull(LANGUAGE_PATH.toFile().listFiles())) {
            if (file.isDirectory()) loadFromFilesInDirectory(file);
            else if (file.getName().endsWith(".json"))
                LANGUAGE_MANAGER.load(file.getName().replace(".json", ""), JacksonParser.createJsonParser(file));
        }
    }

}
