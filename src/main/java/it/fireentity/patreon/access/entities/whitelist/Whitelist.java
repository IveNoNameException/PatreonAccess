package it.fireentity.patreon.access.entities.whitelist;

import it.fireentity.patreon.access.utils.PluginFile;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Whitelist {
    private final List<String> whitelistedPlayers;
    private final PluginFile whitelist;
    private final StorageUtility<List<String>> storageUtility;

    public Whitelist(PluginFile whitelist, StorageUtility<List<String>> storageUtility) {
        this.storageUtility = storageUtility;
        this.whitelist = whitelist;
        whitelistedPlayers = new ArrayList<>();
        whitelistedPlayers.addAll(whitelist.getConfig().getStringList("whitelisted_players"));
    }

    public void addPlayer(String player) {
        whitelistedPlayers.add(player);
        storageUtility.set(whitelistedPlayers);
    }

    public void removePlayer(String player) {
        whitelistedPlayers.remove(player);
        storageUtility.set(whitelistedPlayers);
    }

    public boolean isWhitelisted(String player) {
        return whitelistedPlayers.contains(player);
    }
}
