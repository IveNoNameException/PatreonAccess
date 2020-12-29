package it.fireentity.patreon.access.cache;

import com.google.gson.Gson;
import it.arenacraft.data.core.api.RedisConnection;
import it.arenacraft.data.core.api.RedisData;
import it.arenacraft.permissions.Base;
import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.entities.PatreonPlayer;
import it.fireentity.patreon.access.entities.PatreonVip;
import it.fireentity.patreon.access.entities.Whitelist;
import it.fireentity.patreon.access.enumerations.Config;
import it.fireentity.patreon.access.storage.mysql.WhitelistDatabaseUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

public class PatreonPlayerCache implements Listener {
    private final HashMap<String, PatreonPlayer> patreonPlayerHashMap = new HashMap<>();
    private final Whitelist whitelist;
    private final WhitelistDatabaseUtility whitelistDatabaseUtility;
    private final PatreonAccess patreonAccess;

    public PatreonPlayerCache(Whitelist whitelist, PatreonAccess patreonAccess, WhitelistDatabaseUtility whitelistDatabaseUtility) {
        this.patreonAccess = patreonAccess;
        this.whitelist = whitelist;
        this.whitelistDatabaseUtility = whitelistDatabaseUtility;
        Bukkit.getPluginManager().registerEvents(this, patreonAccess);
    }

    @EventHandler
    public void onPlayerJoinAsync(AsyncPlayerPreLoginEvent event) {
        if(patreonPlayerHashMap.get(event.getName()) == null) {
            RedisConnection redisConnection = RedisData.getConnection();
            String json = redisConnection.get("patreonaccess:players:" + event.getName());
            Optional<PatreonPlayer> patreonPlayer;
            if(json == null) {
                patreonPlayer = whitelistDatabaseUtility.loadPlayer(event.getName());
                patreonPlayer.ifPresent(player -> redisConnection.set("patreonaccess:players:" + event.getName(), new Gson().toJson(player)));
            } else {
                Gson gson = new Gson();
                patreonPlayer = Optional.of(gson.fromJson(json, PatreonPlayer.class));
                patreonPlayer.get().setJoinedTime(System.currentTimeMillis());
            }
            patreonPlayer.ifPresent(player -> patreonPlayerHashMap.put(player.getPlayerName(), player));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {
        if (whitelist.isWhitelisted(event.getPlayer().getName())) {
            if (patreonPlayerHashMap.get(event.getPlayer().getName()).isExceeded()) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, patreonAccess.getLocales().getString(Config.KICK_MESSAGE.getPath()));
                patreonPlayerHashMap.remove(event.getPlayer().getName());
                return;
            }
            if(patreonAccess.getLocales().hasPath(Config.JOIN_PERMISSION.getPath())) {
                Base.addTempPermission(event.getPlayer(), Config.JOIN_PERMISSION.getPath());
            } else {
                System.out.println(patreonAccess.getLocales().getString(Config.JOIN_PERMISSION.getPath()));
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, patreonAccess.getLocales().getString(Config.KICK_MESSAGE.getPath()));
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Optional<PatreonPlayer> patreonPlayer = getPlayer(event.getPlayer());
        if(patreonPlayer.isPresent()) {
            patreonPlayer.get().setCurrentPlayedTime(patreonPlayer.get().onlinePlayedTime() + patreonPlayer.get().getCurrentPlayedTime());
            RedisConnection redisConnection = RedisData.getConnection();
            patreonPlayer.ifPresent(value -> redisConnection.set("patreonaccess:players:" + event.getPlayer().getName(), new Gson().toJson(patreonPlayer.get())));
            Bukkit.getScheduler().runTaskAsynchronously(patreonAccess, () -> {
                whitelistDatabaseUtility.insertPatreonPlayer(patreonPlayer.get());
                patreonPlayerHashMap.remove(patreonPlayer.get().getPlayerName());
            });
        }
    }

    public Collection<PatreonPlayer> getPlayers() {
        return patreonPlayerHashMap.values();
    }

    public Optional<PatreonPlayer> getPlayer(Player player) {
        return Optional.ofNullable(patreonPlayerHashMap.get(player.getName()));
    }

    public void addPlayer(PatreonPlayer patreonPlayer) {
        patreonPlayerHashMap.put(patreonPlayer.getPlayerName(), patreonPlayer);
    }

    public void addPlayer(String patreonPlayer, String vipName) {
        Optional<PatreonVip> patreonVip = patreonAccess.getPatreonVipCache().getPatreonVip(vipName);
        patreonVip.ifPresent(vip -> patreonPlayerHashMap.put(patreonPlayer, new PatreonPlayer(vip, patreonPlayer, 0)));
    }

    public void removePlayer(PatreonPlayer patreonPlayer) {
        patreonPlayerHashMap.remove(patreonPlayer.getPlayerName());
    }
}