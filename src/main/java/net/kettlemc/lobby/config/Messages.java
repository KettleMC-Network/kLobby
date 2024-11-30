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

    private static final Path LANGUAGE_PATH = Paths.get("plugins", "Plugin", "languages");
    private static final Slams LANGUAGE_MANAGER = Slams.create(DEFAULT_LANGUAGE);

    public static final AdventureMessage PREFIX = AdventureMessage.of("miscellaneous.prefix", LANGUAGE_MANAGER);
    public static final AdventureMessage NO_PERMISSION = AdventureMessage.of("miscellaneous.no-permission", LANGUAGE_MANAGER);
    public static final AdventureMessage PLAYER_ONLY = AdventureMessage.of("miscellaneous.player-only", LANGUAGE_MANAGER);
    public static final AdventureMessage PLAYER_NOT_FOUND = AdventureMessage.of("miscellaneous.player-not-found", LANGUAGE_MANAGER);

    public static final AdventureMessage BUILD_ENABLED = AdventureMessage.of("command.build.enabled", LANGUAGE_MANAGER);
    public static final AdventureMessage BUILD_ENABLED_OTHER = AdventureMessage.of("command.build.enabled-other", LANGUAGE_MANAGER);
    public static final AdventureMessage BUILD_DISABLED = AdventureMessage.of("command.build.disabled", LANGUAGE_MANAGER);
    public static final AdventureMessage BUILD_DISABLED_OTHER = AdventureMessage.of("command.build.disabled-other", LANGUAGE_MANAGER);

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
