package net.kettlemc.lobby.util;

import java.util.ArrayList;
import java.util.List;

public class LobbyUtil {

    private LobbyUtil() {
    }

    public static String[] parseQuotationMarkArguments(String[] args) {
        List<String> arguments = new ArrayList<>();
        StringBuilder currentArg = new StringBuilder();
        boolean insideQuotes = false;

        for (String token : args) {
            int i = 0;
            while (i < token.length()) {
                char c = token.charAt(i);

                if (c == '"' && (i == 0 || token.charAt(i - 1) != '\\')) {
                    insideQuotes = !insideQuotes;
                    if (!insideQuotes) {
                        arguments.add(currentArg.toString());
                        currentArg.setLength(0);
                    }
                } else if (insideQuotes) {
                    if (c == '\\' && i + 1 < token.length() && token.charAt(i + 1) == '"') {
                        currentArg.append('"');
                        i++;
                    } else if (c == '\\' && i + 1 < token.length() && token.charAt(i + 1) == '\\') {
                        currentArg.append('\\');
                        i++;
                    } else {
                        currentArg.append(c);
                    }
                } else {
                    if (c == '\\' && i + 1 < token.length() && token.charAt(i + 1) == ' ') {
                        currentArg.append(" ");
                        i++;
                    } else {
                        currentArg.append(c);
                    }
                }
                i++;
            }

            if (!insideQuotes && currentArg.length() > 0) {
                arguments.add(currentArg.toString());
                currentArg.setLength(0);
            } else if (insideQuotes) {
                currentArg.append(" ");
            }
        }

        if (insideQuotes) {
            throw new IllegalArgumentException("Unmatched quotes in input.");
        }

        return arguments.toArray(new String[0]);
    }

}
