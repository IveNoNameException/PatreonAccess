package it.fireentity.patreon.access.enumerations;

public enum Config {

    PATREON_VIP_LINE,
    JOIN_PERMISSION,
    KICK_MESSAGE,
    PLAYER_ALREADY_ADDED,
    PLAYER_ALREADY_REMOVED,
    PATREON_ONLINE_TIME,
    REDIS_CHANNEL,
    TIME_PLACEHOLDER,
    FALLBACK_PLACEHOLDER_STRING,
    PATREON_TIME_EXPIRED_STRING,
    PATREON_PLAYER_NOT_FOUND,
    KICK_ERROR;

    public String getPath() {
        return this.name().toLowerCase();
    }
}
