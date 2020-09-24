package it.fireentity.patreon.access.enumerations;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Patreon {
    PATREON_NAME("%patreon.display_name"),
    PATREON_TIME("%patreon.max_online_time");

    private final String message;
    private String modifiedPath;
    @Getter
    private static YamlConfiguration config;

    Patreon(String message) {
        modifiedPath = message;
        this.message = message;
    }

    public static void setConfiguration(YamlConfiguration config) {
        Patreon.config = config;
    }

    public String getMessage() {
        String msg = config.getString(modifiedPath);
        if (msg == null) return modifiedPath;
        msg = msg.replace("&", "§");
        return msg;
    }

    public long getLong() {
        long value = config.getLong(modifiedPath, -1);
        if (value == -1) {
            System.out.println("Messaggio non trovato: " + modifiedPath);
            return -1;
        }

        return value;
    }

    public void reset() {
        modifiedPath = message;
    }


    public Patreon setPlaceholderAndReset(String placeHolder, String substitution) {
        reset();
        modifiedPath = modifiedPath.replace(placeHolder, substitution);
        return this;
    }

    public String getPath() {
        return modifiedPath;
    }
}
