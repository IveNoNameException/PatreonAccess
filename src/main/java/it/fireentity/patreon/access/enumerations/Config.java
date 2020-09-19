package it.fireentity.patreon.access.enumerations;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum Config {
    ONLINE_TIME_CHECK_FREQUENCY("online_time_check_frequency"),
    JOIN_PERMISSION("join_permission"),
    KICK_MESSAGE("kick_message");

    private final String message;
    private String modifiedPath;
    @Getter
    private static YamlConfiguration config;

    Config(String message) {
        modifiedPath = message;
        this.message = message;
    }

    public static void setConfiguration(YamlConfiguration config) {
        Config.config = config;
    }

    public String getMessage() {
        String msg = config.getString(modifiedPath);
        if (msg == null) return modifiedPath;
        msg = msg.replace("&", "§");
        return msg;
    }

    public int getInt() {
        int value = config.getInt(modifiedPath, -1);
        if (value == -1) {
            System.out.println("Messaggio non trovato: " + modifiedPath);
            return -1;
        }

        return value;
    }

    public List<String> getMessageList() {
        List<String> messages = config.getStringList(modifiedPath);

        if(messages == null) {
            System.out.println("Messaggio non trovato: " + modifiedPath);
            return new ArrayList<>();
        }

        messages = messages.stream().map(message -> message.replace("&", "§")).collect(Collectors.toList());
        return messages;
    }

    public void reset() {
        modifiedPath = message;
    }


    public Config setPlaceholderAndReset(String placeHolder, String substitution) {
        reset();
        modifiedPath = modifiedPath.replace(placeHolder, substitution);
        return this;
    }

    public String getPath() {
        return modifiedPath;
    }
}
