package it.fireentity.patreon.access.enumerations;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum Config {
    JOIN_PERMISSION("join_permission"),
    KICK_MESSAGE("kick_message"),
    INSUFFICIENT_PERMISSIONS("insufficient_permissions"),
    INVALID_COMMAND_SENDER("invalid_command_sender"),
    ADD_COMMAND_USAGE("add_command_usage"),
    PLAYER_ALREADY_ADDED("player_already_added"),
    ADD_COMMAND_SUCCESS("add_command_success"),
    REMOVE_COMMAND_USAGE("remove_command_usage"),
    REMOVE_COMMAND_SUCCESS("remove_command_success"),
    PATREON_USAGE("patreon_usage"),
    PATREON_NOT_FOUND("patreon_not_found"),
    PATREON_ONLINE_TIME("patreon_online_time"),
    REDIS_CHANNEL("redis_channel"),
    TIME_PLACEHOLDER("time_placeholder"),
    FALLBACK_PLACEHOLDER_STRING("fallback_placeholder_string"),
    PATREON_TIME_EXPIRED_STRING("patreon_time_expired_string"),
    PATREON_PLAYER_NOT_FOUND("patreon_player_not_found"),
    PATREON_NOT_WHITELISTED("patreon_not_whitelisted");

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
