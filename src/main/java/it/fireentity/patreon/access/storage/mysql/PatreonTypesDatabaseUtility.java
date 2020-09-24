package it.fireentity.patreon.access.storage.mysql;

import it.arenacraft.data.core.api.MysqlConnection;
import it.arenacraft.data.core.api.MysqlData;
import it.fireentity.patreon.access.enumerations.Query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatreonTypesDatabaseUtility {
    public PatreonTypesDatabaseUtility() {
        try (MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            mysqlConnection.executePreparedUpdate(Query.CREATE_PATREON_TYPES_TABLE.getQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(String patreonType) {
        try(MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            mysqlConnection.executePreparedUpdate(Query.INSERT_PATREON_TYPE.getQuery(), patreonType, patreonType);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remove(String patreonType) {
        try (MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            mysqlConnection.executePreparedUpdate(Query.DELETE_PATREON_TYPE.getQuery(), patreonType);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> loadPatreonVips() {
        List<String> patreonTypes = new ArrayList<>();
        try(MysqlConnection mysqlConnection = MysqlData.getConnection()) {
            MysqlConnection.PreparedResult preparedResult = mysqlConnection.executePreparedQuery(Query.SELECT_PATREON_TYPES.getQuery());
            while(preparedResult.getResult().next()) {
                patreonTypes.add(preparedResult.getResult().getString("patreonType"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patreonTypes;
    }
}