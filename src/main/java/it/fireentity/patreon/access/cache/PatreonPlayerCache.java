package it.fireentity.patreon.access.cache;

import com.google.gson.Gson;
import it.arenacraft.data.core.api.RedisConnection;
import it.arenacraft.data.core.api.RedisData;
import it.arenacraft.permissions.Base;
import it.fireentity.library.storage.Cache;
import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.entities.PatreonPlayer;
import it.fireentity.patreon.access.enumerations.Config;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;
import java.util.Optional;

public class PatreonPlayerCache implements Listener {
    private final Cache<String, PatreonPlayer> cache = new Cache<>();
    private final PatreonAccess patreonAccess;

    public PatreonPlayerCache(PatreonAccess patreonAccess) {
        this.patreonAccess = patreonAccess;
        Bukkit.getPluginManager().registerEvents(this, patreonAccess);
    }

    @EventHandler
    public void onPlayerJoinAsync(AsyncPlayerPreLoginEvent event) {
        if (!cache.getValue(event.getName()).isPresent()) {
            RedisConnection redisConnection = RedisData.getConnection();
            String json = redisConnection.get("patreonaccess:players:" + event.getName());
            Optional<PatreonPlayer> patreonPlayer;
            if (json == null) {
                patreonPlayer = patreonAccess.getPatreonPlayersDatabaseUtility().select(event.getName());
            } else {
                Gson gson = new Gson();
                patreonPlayer = Optional.of(gson.fromJson(json, PatreonPlayer.class));
                patreonPlayer.get().setJoinedTime(System.currentTimeMillis());
            }
            patreonPlayer.ifPresent(patreon -> cache.addValue(patreonPlayer.get()));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerLoginEvent event) {
        if (cache.getValue(event.getPlayer().getName()).map(PatreonPlayer::isExceeded).orElse(false)) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, patreonAccess.getLocales().getString(Config.KICK_MESSAGE.getPath()));
            return;
        }
        if (patreonAccess.getLocales().hasPath(Config.JOIN_PERMISSION.getPath())) {
            Base.addTempPermission(event.getPlayer(), Config.JOIN_PERMISSION.getPath());
        } else {
            patreonAccess.getServer().getLogger().severe(patreonAccess.getLocales().getString(Config.JOIN_PERMISSION.getPath()));
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, patreonAccess.getLocales().getString(Config.KICK_ERROR.getPath()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Optional<PatreonPlayer> patreonPlayer = cache.getValue(event.getPlayer().getName());
        if (patreonPlayer.isPresent()) {
            patreonPlayer.get().setCurrentPlayedTime(patreonPlayer.get().onlinePlayedTime() + patreonPlayer.get().getCurrentPlayedTime());
            try (RedisConnection redisConnection = RedisData.getConnection()) {
                patreonPlayer.ifPresent(value -> redisConnection.set("patreonaccess:players:" + event.getPlayer().getName(), new Gson().toJson(patreonPlayer.get())));
                cache.removeValue(patreonPlayer.get().getKey());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Collection<PatreonPlayer> getPlayers() {
        return cache.getValues();
    }

    public Optional<PatreonPlayer> getPlayer(String name) {
        return cache.getValue(name);
    }

    public void removePlayer(PatreonPlayer patreonPlayer) {
        cache.removeValue(patreonPlayer.getKey());
    }
}