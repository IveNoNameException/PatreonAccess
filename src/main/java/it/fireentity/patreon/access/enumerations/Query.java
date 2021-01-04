package it.fireentity.patreon.access.enumerations;

import lombok.Getter;

public enum Query {
    CREATE_PLAYERS_TABLE("CREATE TABLE IF NOT EXISTS `hub_patreonaccess_players`(`id` INT AUTO_INCREMENT PRIMARY KEY NOT NULL, `player` VARCHAR(16) UNIQUE NOT NULL)"),
    CRATE_PATREON_PLAYERS_TABLE("CREATE TABLE IF NOT EXISTS `hub_patreonaccess_patreon_players`(" +
            "`player` INT NOT NULL, " +
            "`patreonType` INT NOT NULL, " +
            "`milliseconds` BIGINT UNSIGNED NOT NULL, " +
            "FOREIGN KEY (`patreonType`) REFERENCES `hub_patreonaccess_patreonTypes`(`id`) ON DELETE CASCADE, " +
            "FOREIGN KEY (`player`) REFERENCES `hub_patreonaccess_players`(`id`) ON DELETE CASCADE, " +
            "PRIMARY KEY (`player`))"),
    CREATE_PATREON_TYPES_TABLE("CREATE TABLE IF NOT EXISTS `hub_patreonaccess_patreonTypes`(`id` INT AUTO_INCREMENT PRIMARY KEY NOT NULL, `patreonType` VARCHAR(30) UNIQUE NOT NULL)"),
    /**
     * Patreon vips queries
     */
    INSERT_PATREON_TYPE("INSERT INTO `hub_patreonaccess_patreonTypes`(`patreonType`) VALUES(?) ON DUPLICATE KEY UPDATE `hub_patreonaccess_patreonTypes`.`patreonType` = ?"),
    DELETE_PATREON_TYPE("DELETE FROM `hub_patreonaccess_patreonTypes` WHERE `hub_patreonaccess_patreonTypes`.`patreonType` = ?"),
    SELECT_PATREON_TYPES("SELECT * FROM `hub_patreonaccess_patreonTypes`"),

    /**
     * Patreon players queries
     *
     */
    INSERT_PATREON_PLAYER("INSERT INTO `hub_patreonaccess_patreon_players` VALUES(" +
            "(SELECT `players`.`id` FROM `hub_patreonaccess_players` `players` WHERE `players`.`player` = ?), " +
            "(SELECT `types`.`id` FROM `hub_patreonaccess_patreontypes` `types` WHERE `types`.`patreonType` = ?), " +
            "?) ON DUPLICATE KEY UPDATE " +
            "`hub_patreonaccess_patreon_players`.`player` = (SELECT `players`.`id` FROM `hub_patreonaccess_players` `players` WHERE `players`.`player` = ?), " +
            "`hub_patreonaccess_patreon_players`.`patreonType` = (SELECT `types`.`id` FROM `hub_patreonaccess_patreontypes` `types` WHERE `types`.`patreonType` = ?), " +
            "`hub_patreonaccess_patreon_players`.`milliseconds` = ?"),
    SELECT_PATREON_PLAYERS("SELECT `hub_patreonaccess_players`.`player`, `hub_patreonaccess_patreonTypes`.`patreonType`, `hub_patreonaccess_patreon_players`.`milliseconds` FROM `hub_patreonaccess_patreon_players` " +
            "JOIN `hub_patreonaccess_players` ON `hub_patreonaccess_patreon_players`.`player` = `hub_patreonaccess_players`.`id` " +
            "JOIN `hub_patreonaccess_patreonTypes` ON `hub_patreonaccess_patreon_players`.`patreonType` = `hub_patreonaccess_patreonTypes`.`id`"),
    SELECT_PATREON_PLAYER("SELECT * FROM `hub_patreonaccess_patreon_players` " +
            "JOIN `hub_patreonaccess_players` ON `hub_patreonaccess_patreon_players`.`player` = `hub_patreonaccess_players`.`id` " +
            "JOIN `hub_patreonaccess_patreonTypes` ON `hub_patreonaccess_patreon_players`.`patreonType` = `hub_patreonaccess_patreonTypes`.`id` " +
            "WHERE `hub_patreonaccess_players`.`player` = ?"),
    DELETE_PATREON_PLAYER("DELETE `hub_patreonaccess_patreon_players` FROM `hub_patreonaccess_patreon_players` " +
            "JOIN `hub_patreonaccess_players` ON `hub_patreonaccess_players`.`id` = `hub_patreonaccess_patreon_players`.`player` " +
            "JOIN `hub_patreonaccess_patreonTypes` ON `hub_patreonaccess_patreonTypes`.`id` = `hub_patreonaccess_patreon_players`.`patreonType` " +
            "WHERE `hub_patreonaccess_players`.`player` = ?"),
    INSERT_PLAYER("INSERT INTO `hub_patreonaccess_players`(`player`) VALUES(?) ON DUPLICATE KEY UPDATE `hub_patreonaccess_players`.`player` = ?");

    @Getter
    private final String query;
    Query(String query) {
        this.query = query;
    }
}
