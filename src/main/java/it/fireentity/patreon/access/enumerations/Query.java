package it.fireentity.patreon.access.enumerations;

import lombok.Getter;

public enum Query {

    /**
     * Creation table queries
     */
    CREATE_WHITELIST_TABLE("CREATE TABLE IF NOT EXISTS hub_patreonaccess_whitelist(" +
            "player INT UNIQUE, FOREIGN KEY (player) REFERENCES hub_patreonaccess_players(id) ON DELETE CASCADE, " +
            "patreonType INT, FOREIGN KEY (patreonType) REFERENCES hub_patreonaccess_patreonTypes(id) ON DELETE CASCADE, " +
            "milliseconds BIGINT UNSIGNED, " +
            "PRIMARY KEY (player, patreonType))"),
    CREATE_PLAYERS_TABLE("CREATE TABLE IF NOT EXISTS hub_patreonaccess_players(id INT AUTO_INCREMENT PRIMARY KEY, player VARCHAR(16) UNIQUE)"),
    CREATE_PATREON_TYPES_TABLE("CREATE TABLE IF NOT EXISTS hub_patreonaccess_patreonTypes(id INT AUTO_INCREMENT PRIMARY KEY, patreonType VARCHAR(30) UNIQUE)"),

    /**
     * Whitelisted players queries
     */
    INSERT_WHITELISTED_PLAYER("INSERT INTO hub_patreonaccess_whitelist(player, patreonType, milliseconds) " +
            "VALUES(" +
            "(SELECT players.id FROM hub_patreonaccess_players players WHERE players.player = ?), " +
            "(SELECT patreonTypes.id FROM hub_patreonaccess_patreonTypes patreonTypes WHERE patreonTypes.patreonType = ?), " +
            "?" +
            ") ON DUPLICATE KEY UPDATE " +
            "player = (SELECT players.id FROM hub_patreonaccess_players players WHERE players.player = ?), " +
            "patreonType = (SELECT patreonTypes.id FROM hub_patreonaccess_patreonTypes patreonTypes WHERE patreonTypes.patreonType = ?)," +
            "milliseconds = ?"),
    /**
     * Patreon vips queries
     */
    INSERT_PATREON_TYPE("INSERT INTO hub_patreonaccess_patreonTypes(patreonType) VALUES(?) ON DUPLICATE KEY UPDATE hub_patreonaccess_patreonTypes.patreonType = ?"),
    DELETE_PATREON_TYPE("DELETE FROM hub_patreonaccess_patreonTypes WHERE hub_patreonaccess_patreonTypes.patreonType = ?"),
    SELECT_PATREON_TYPES("SELECT * FROM hub_patreonaccess_patreonTypes"),

    /**
     * Patreon players queries
     */
    SELECT_PATREON_PLAYERS("SELECT hub_patreonaccess_players.player, hub_patreonaccess_patreonTypes.patreonType, hub_patreonaccess_whitelist.milliseconds FROM hub_patreonaccess_whitelist " +
            "JOIN hub_patreonaccess_players ON hub_patreonaccess_whitelist.player = hub_patreonaccess_players.id " +
            "JOIN hub_patreonaccess_patreonTypes ON hub_patreonaccess_whitelist.patreonType = hub_patreonaccess_patreonTypes.id"),
    SELECT_PATREON_PLAYER("SELECT * FROM hub_patreonaccess_whitelist " +
            "JOIN hub_patreonaccess_players ON hub_patreonaccess_whitelist.player = hub_patreonaccess_players.id " +
            "JOIN hub_patreonaccess_patreonTypes ON hub_patreonaccess_whitelist.patreonType = hub_patreonaccess_patreonTypes.id " +
            "WHERE hub_patreonaccess_players.player = ?"),
    DELETE_PATREON_PLAYER("DELETE hub_patreonaccess_whitelist FROM hub_patreonaccess_whitelist " +
            "JOIN hub_patreonaccess_players ON hub_patreonaccess_players.id = hub_patreonaccess_whitelist.player " +
            "JOIN hub_patreonaccess_patreonTypes ON hub_patreonaccess_patreonTypes.id = hub_patreonaccess_whitelist.patreonType " +
            "WHERE hub_patreonaccess_players.player = ?"),
    INSERT_PLAYER("INSERT INTO hub_patreonaccess_players(player) VALUES(?) ON DUPLICATE KEY UPDATE hub_patreonaccess_players.player = ?");

    @Getter
    private final String query;
    Query(String query) {
        this.query = query;
    }
}
