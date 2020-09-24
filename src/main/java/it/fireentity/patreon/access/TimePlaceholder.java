package it.fireentity.patreon.access;

import it.fireentity.patreon.access.entities.player.PatreonPlayer;
import it.fireentity.patreon.access.enumerations.Config;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.sql.Time;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class TimePlaceholder extends PlaceholderExpansion {

    private final PatreonAccess patreonAccess;

    public TimePlaceholder(PatreonAccess patreonAccess) {
        this.patreonAccess = patreonAccess;
    }

    @Override
    public String getIdentifier() {
        return Config.TIME_PLACEHOLDER.getMessage();
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
            return Config.FALLBACK_PLACEHOLDER_STRING.getMessage();
        }
        return patreonPlayer.get().getOnlineTime();
    }
}
