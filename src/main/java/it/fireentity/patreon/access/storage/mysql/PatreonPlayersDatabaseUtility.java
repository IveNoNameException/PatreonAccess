package it.fireentity.patreon.access.storage.mysql;

import it.arenacraft.data.core.api.MysqlConnection;
import it.arenacraft.data.core.api.MysqlData;
import it.fireentity.library.storage.LoadableDatabaseUtility;
import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.entities.PatreonPlayer;
import it.fireentity.patreon.access.entities.PatreonVip;
import it.fireentity.patreon.access.enumerations.Query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PatreonPlayersDatabaseUtility extends LoadableDatabaseUtility<PatreonPlayer, String> {
    private final PatreonAccess patreonAccess;

    public PatreonPlayersDatabaseUtility(PatreonAccess patreonAccess) {
        this.patreonAccess = patreonAccess;
        try(MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            mysqlConnection.executePreparedUpdate(Query.CRATE_PATREON_PLAYERS_TABLE.getQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<PatreonPlayer> select(String key) {
        try(MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            ResultSet resultSet = mysqlConnection.executePreparedQuery(Query.SELECT_PATREON_PLAYER.getQuery(), key).getResult();
            if(resultSet.next()) {
                String patreonType = resultSet.getString("hub_patreonaccess_patreonTypes.patreonType");
                String playerName = resultSet.getString("hub_patreonaccess_players.player");
                long milliseconds = resultSet.getLong("hub_patreonaccess_patreon_players.milliseconds");
                Optional<PatreonVip> patreonVip = patreonAccess.getPatreonVipsDatabaseUtility().select(patreonType);
                return patreonVip.map(vip -> new PatreonPlayer(vip,playerName, milliseconds));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void insert(PatreonPlayer patreonPlayer) {
        //Insert the player in the players table
        patreonAccess.getPlayerDatabaseUtility().insert(patreonPlayer);

        try(MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            mysqlConnection.executePreparedUpdate(Query.INSERT_PATREON_PLAYER.getQuery(),patreonPlayer.getKey(),patreonPlayer.getPatreonVip().getKey(),0,patreonPlayer.getKey(),patreonPlayer.getPatreonVip().getKey(),0);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void remove(PatreonPlayer patreonPlayer) {
        try(MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            mysqlConnection.executePreparedUpdate(Query.DELETE_PATREON_PLAYER.getQuery(), patreonPlayer.getKey());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}