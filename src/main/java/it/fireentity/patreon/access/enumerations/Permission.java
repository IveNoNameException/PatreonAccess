package it.fireentity.patreon.access.enumerations;

import org.bukkit.configuration.file.YamlConfiguration;

public enum Permission {
    TIME_COMMAND_PERMISSION("time_command_permission"),
    ADD_COMMAND_PERMISSION("add_command_permission"),
    REMOVE_COMMAND_PERMISSION("remove_command_permission"),
    PATREON_MAIN_COMMAND_PERMISSION("patreon_main_command_permission"),
    PATREON_ACCESS_TIP_PERMISSION("patreon_access_tip_permission");


    private static YamlConfiguration config;
    private final String path;

    Permission(String path) {
        this.path = path;
    }

    public String getPermission() {
        return config.getString(path);
    }

    public static void setConfiguration(YamlConfiguration config) {
        Permission.config = config;
    }
}
