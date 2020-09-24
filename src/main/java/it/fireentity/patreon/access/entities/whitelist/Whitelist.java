package it.fireentity.patreon.access.entities.whitelist;

import it.fireentity.patreon.access.cache.PatreonPlayerCache;
import it.fireentity.patreon.access.storage.mysql.WhitelistDatabaseUtility;
import it.fireentity.patreon.access.entities.player.PatreonPlayer;
import it.fireentity.patreon.access.entities.vip.PatreonVip;
import it.fireentity.patreon.access.utils.PluginFile;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Getter
public class Whitelist {
    private final List<String> whitelistedPlayers;
    private final PluginFile whitelist;
    private final WhitelistDatabaseUtility whitelistDatabaseUtility;

    public Whitelist(PluginFile whitelist, WhitelistDatabaseUtility whitelistDatabaseUtility) {
        this.whitelistDatabaseUtility = whitelistDatabaseUtility;
        this.whitelist = whitelist;
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
