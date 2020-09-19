package it.fireentity.patreon.access.thread;

import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.entities.player.PatreonPlayer;
import it.fireentity.patreon.access.cache.PatreonPlayerCache;
import it.fireentity.patreon.access.enumerations.Config;
import org.bukkit.scheduler.BukkitRunnable;

public class OnlineTimeChecker extends BukkitRunnable {

    private final PatreonPlayerCache patreonPlayerCache;

    public OnlineTimeChecker(PatreonAccess patreonAccess) {
        this.runTaskTimerAsynchronously(patreonAccess, 0, Config.ONLINE_TIME_CHECK_FREQUENCY.getInt());
        patreonPlayerCache = patreonAccess.getPatreonPlayerCache();
    }

    @Override
    public void run() {
        for(PatreonPlayer patreonPlayer : patreonPlayerCache.getPlayers()) {

        }
    }
}
