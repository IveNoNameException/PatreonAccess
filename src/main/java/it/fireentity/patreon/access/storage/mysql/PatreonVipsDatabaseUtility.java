package it.fireentity.patreon.access.storage.mysql;

import it.arenacraft.data.core.api.MysqlConnection;
import it.arenacraft.data.core.api.MysqlData;
import it.fireentity.library.storage.LoadableDatabaseUtility;
import it.fireentity.patreon.access.entities.PatreonVip;
import it.fireentity.patreon.access.enumerations.Query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatreonVipsDatabaseUtility extends LoadableDatabaseUtility<PatreonVip, String> {

    public PatreonVipsDatabaseUtility() {
        try (MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            mysqlConnection.executePreparedUpdate(Query.CREATE_PATREON_TYPES_TABLE.getQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<PatreonVip> load() {
        List<PatreonVip> patreonTypes = new ArrayList<>();
        try(MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            MysqlConnection.PreparedResult preparedResult = mysqlConnection.executePreparedQuery(Query.SELECT_PATREON_TYPES.getQuery());
            while(preparedResult.getResult().next()) {
                PatreonVip patreonVip = new PatreonVip(preparedResult.getResult().getString("patreonType"),"",0);
                patreonTypes.add(patreonVip);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patreonTypes;
    }

    @Override
    public void insert(PatreonVip patreon) {
        try(MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            mysqlConnection.executePreparedUpdate(Query.INSERT_PATREON_TYPE.getQuery(), patreon.getKey(), patreon.getKey());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(PatreonVip patreon) {
        try (MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            mysqlConnection.executePreparedUpdate(Query.DELETE_PATREON_TYPE.getQuery(), patreon.getKey());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<PatreonVip> select(String patreonKey) {
        return Optional.empty();
    }
}