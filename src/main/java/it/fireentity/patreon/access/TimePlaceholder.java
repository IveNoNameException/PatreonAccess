package it.fireentity.patreon.access;

import it.fireentity.patreon.access.entities.PatreonPlayer;
import it.fireentity.patreon.access.enumerations.Config;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.util.Optional;

public class TimePlaceholder extends PlaceholderExpansion {

    private final PatreonAccess patreonAccess;

    public TimePlaceholder(PatreonAccess patreonAccess) {
        this.patreonAccess = patreonAccess;
    }

    @Override
    public String getIdentifier() {
        return patreonAccess.getLocales().getString(Config.TIME_PLACEHOLDER.getPath());
    }

    @Override
    public String getAuthor() {
        return "Fireentity_293_";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        Optional<PatreonPlayer> patreonPlayer = patreonAccess.getPatreonPlayerCache().getPlayer(player.getPlayer());
        if(!patreonPlayer.isPresent()) {
            return patreonAccess.getLocales().getString(Config.FALLBACK_PLACEHOLDER_STRING.getPath());
        }
        if(patreonPlayer.get().getOnlineTime().isPresent()) {
            return patreonPlayer.get().getOnlineTime().get();
        } else {
            return patreonAccess.getLocales().getString(Config.PATREON_TIME_EXPIRED_STRING.getPath());
        }
    }
}
