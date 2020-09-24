package it.fireentity.patreon.access.storage.mysql;

import it.arenacraft.data.core.api.MysqlConnection;
import it.arenacraft.data.core.api.MysqlData;
import it.fireentity.patreon.access.enumerations.Query;

import java.sql.SQLException;

public class PlayersDatabaseUtility {
    public PlayersDatabaseUtility() {
        try(MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            mysqlConnection.executePreparedUpdate(Query.CREATE_PLAYERS_TABLE.getQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertPlayer(String player ) {
        try(MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            mysqlConnection.executePreparedUpdate(Query.INSERT_PLAYER.getQuery(), player, player);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
