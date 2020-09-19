package it.fireentity.patreon.access.cache;

import it.arenacraft.permissions.Base;
import it.fireentity.patreon.access.entities.player.PatreonPlayer;
import it.fireentity.patreon.access.entities.whitelist.Whitelist;
import it.fireentity.patreon.access.enumerations.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Collection;
import java.util.HashMap;

public class PatreonPlayerCache implements Listener {
    private final HashMap<String, PatreonPlayer> patreonPlayerHashMap = new HashMap<>();
    private final Whitelist whitelist;

    public PatreonPlayerCache(Whitelist whitelist) {
        this.whitelist = whitelist;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(whitelist.isWhitelisted(event.getPlayer().getName())) {
            PatreonPlayer patreonPlayer = patreonPlayerHashMap.get(event.getPlayer().getName());
            if(patreonPlayer != null) {
                if(patreonPlayer.isExceeded()) {
                    event.getPlayer().kickPlayer(Config.KICK_MESSAGE.getMessage());
                    return;
                }
                Base.addTempPermission(event.getPlayer(), Config.JOIN_PERMISSION.getMessage());
            }
        }
    }

    public Collection<PatreonPlayer> getPlayers() {
        return patreonPlayerHashMap.values();
    }
}