package it.fireentity.patreon.access.storage.mysql;

import it.arenacraft.data.core.api.MysqlConnection;
import it.arenacraft.data.core.api.MysqlData;
import it.fireentity.library.player.CustomPlayer;
import it.fireentity.library.storage.LoadableDatabaseUtility;
import it.fireentity.patreon.access.entities.PatreonPlayer;
import it.fireentity.patreon.access.enumerations.Query;

import java.sql.SQLException;
import java.util.Optional;

public class PlayerDatabaseUtility extends LoadableDatabaseUtility<PatreonPlayer, String> {
    public PlayerDatabaseUtility() {
        try(MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            mysqlConnection.executePreparedUpdate(Query.CREATE_PLAYERS_TABLE.getQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(PatreonPlayer patreonPlayer) {
        try(MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            mysqlConnection.executePreparedUpdate(Query.INSERT_PLAYER.getQuery(), patreonPlayer.getKey(), patreonPlayer.getKey());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
