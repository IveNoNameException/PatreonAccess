package it.fireentity.patreon.access.storage.mysql;

import it.arenacraft.data.core.api.MysqlConnection;
import it.arenacraft.data.core.api.MysqlData;
import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.cache.PatreonVipCache;
import it.fireentity.patreon.access.entities.PatreonPlayer;
import it.fireentity.patreon.access.entities.PatreonVip;
import it.fireentity.patreon.access.enumerations.Query;
import lombok.Getter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class WhitelistDatabaseUtility {

    private final PatreonVipCache patreonVipCache;
    private final PlayersDatabaseUtility playersDatabaseUtility;
    private final PatreonVipsDatabaseUtility patreonVipsDatabaseUtility;
    private final PatreonAccess patreonAccess;

    public WhitelistDatabaseUtility(PatreonAccess patreonAccess) {
        this.patreonAccess = patreonAccess;
        playersDatabaseUtility = new PlayersDatabaseUtility();
        patreonVipsDatabaseUtility = new PatreonVipsDatabaseUtility();
        this.patreonVipCache = patreonAccess.getPatreonVipCache();
        try (MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            mysqlConnection.executePreparedUpdate(Query.CREATE_WHITELIST_TABLE.getQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertPatreonPlayer(PatreonPlayer patreonPlayer) {
        playersDatabaseUtility.insertPlayer(patreonPlayer.getPlayerName());

        try(MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            mysqlConnection.executePreparedUpdate(Query.INSERT_WHITELISTED_PLAYER.getQuery(),
                    patreonPlayer.getPlayerName(),
                    patreonPlayer.getPatreonVip().getKey(),
                    patreonPlayer.getCurrentPlayedTime(),
                    patreonPlayer.getPlayerName(),
                    patreonPlayer.getPatreonVip().getKey(),
                    patreonPlayer.getCurrentPlayedTime());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePatreonPlayer(String player) {
        try(MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            mysqlConnection.executePreparedUpdate(Query.DELETE_PATREON_PLAYER.getQuery(), player);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> loadPlayers() {
        List<String> patreonPlayers = new ArrayList<>();
        try(MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            MysqlConnection.PreparedResult preparedResult = mysqlConnection.executePreparedQuery(Query.SELECT_PATREON_PLAYERS.getQuery());
            while(preparedResult.getResult().next()) {
                patreonPlayers.add(preparedResult.getResult().getString("hub_patreonaccess_players.player"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patreonPlayers;
    }

    public Optional<PatreonPlayer> loadPlayer(String player) {
        try(MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            MysqlConnection.PreparedResult preparedResult = mysqlConnection.executePreparedQuery(Query.SELECT_PATREON_PLAYER.getQuery(), player);
            if(preparedResult.getResult().next()) {
                String patreon = preparedResult.getResult().getString("hub_patreonaccess_patreonTypes.patreonType");
                long currentPlayedTime = preparedResult.getResult().getLong("hub_patreonaccess_whitelist.milliseconds");
                Optional<PatreonVip> patreonVip = patreonAccess.getPatreonVipCache().getPatreonVip(patreon);
                if(patreonVip.isPresent()) {
                    return Optional.of(new PatreonPlayer(patreonVip.get(), player, currentPlayedTime));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<PatreonPlayer> loadPatreonPlayers() {
        List<PatreonPlayer> patreonPlayers = new ArrayList<>();
        try(MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            MysqlConnection.PreparedResult preparedResult = mysqlConnection.executePreparedQuery(Query.SELECT_PATREON_PLAYERS.getQuery());
            if(preparedResult.getResult().next()) {
                String playerName = preparedResult.getResult().getString("hub_patreonaccess_players.player");
                String vipName = preparedResult.getResult().getString("hub_patreonaccess_patreonTypes.patreonType");
                long milliseconds = preparedResult.getResult().getLong("hub_patreonaccess_whitelist.milliseconds");
                Optional<PatreonVip> patreonVip = patreonAccess.getPatreonVipCache().getPatreonVip(vipName);
                patreonVip.ifPresent(vip -> patreonPlayers.add(new PatreonPlayer(vip, playerName, milliseconds)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patreonPlayers;
    }
}
