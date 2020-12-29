package it.fireentity.patreon.access.entities;

import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.storage.mysql.WhitelistDatabaseUtility;
import lombok.Getter;

import java.util.List;

@Getter
public class Whitelist {
    private final List<String> whitelistedPlayers;
    private final WhitelistDatabaseUtility whitelistDatabaseUtility;
    private final PatreonAccess patreonAccess;

    public Whitelist(PatreonAccess patreonAccess, WhitelistDatabaseUtility whitelistDatabaseUtility) {
        this.patreonAccess = patreonAccess;
        this.whitelistDatabaseUtility = whitelistDatabaseUtility;
        whitelistedPlayers = whitelistDatabaseUtility.loadPlayers();
        whitelistedPlayers.addAll(whitelistDatabaseUtility.loadPlayers());
    }

    public boolean addPlayerWithDatabase(PatreonVip patreonVip, String player) {
        PatreonPlayer patreonPlayer = new PatreonPlayer(patreonVip, player, 0);
        if (!whitelistedPlayers.contains(player)) {
            whitelistedPlayers.add(player);
            whitelistDatabaseUtility.insertPatreonPlayer(patreonPlayer);
            return true;
        }
        return false;
    }

    public boolean addPlayer(String player) {
        if (!whitelistedPlayers.contains(player)) {
            whitelistedPlayers.add(player);
            return true;
        }
        return false;
    }

    public void removePlayer(String player) {
        whitelistedPlayers.remove(player);
        whitelistDatabaseUtility.deletePatreonPlayer(player);
    }

    public boolean isWhitelisted(String player) {
        return whitelistedPlayers.contains(player);
    }
}
