package it.fireentity.patreon.access.storage.redis;

import com.google.gson.Gson;
import it.arenacraft.data.core.api.RedisConnection;
import it.arenacraft.data.core.api.RedisData;
import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.entities.PatreonPlayer;
import it.fireentity.patreon.access.entities.PatreonVip;
import it.fireentity.patreon.access.enumerations.Config;
import org.bukkit.ChatColor;
import redis.clients.jedis.JedisPubSub;

import java.util.Optional;

public class RedisPubSub extends JedisPubSub implements Runnable {
    private static final Integer DELAY = 1;
    private final PatreonAccess patreonAccess;

    public RedisPubSub(PatreonAccess patreonAccess) {
        this.patreonAccess = patreonAccess;
    }

    public void sendMessage(String message) {
        RedisConnection redisConnection = RedisData.getConnection();
        redisConnection.publish(patreonAccess.getLocales().getString(Config.REDIS_CHANNEL.getPath()), message);
    }

    @Override
    public void onMessage(String s, String s1) {
        Gson gson = new Gson();
        PatreonPlayer patreonPlayer = gson.fromJson(s1, PatreonPlayer.class);
        String patreonName = patreonPlayer.getPatreonVip().getKey();
        Optional<PatreonVip> patreonVip = patreonAccess.getPatreonVipCache().getPatreonVip(patreonName);
        if(patreonVip.isPresent()) {
            patreonAccess.getWhitelist().addPlayer(patreonPlayer.getPlayerName());
        } else {
            patreonAccess.getLogger().severe(ChatColor.RED + "Unable to whitelist this player! Unsynchronized patreon name, check the config");
        }
    }

    @Override
    public void onPMessage(String s, String s1, String s2) {
        
    }

    @Override
    public void onSubscribe(String s, int i) {

    }

    @Override
    public void onUnsubscribe(String s, int i) {

    }

    @Override
    public void onPUnsubscribe(String s, int i) {

    }

    @Override
    public void onPSubscribe(String s, int i) {

    }

    @Override
    public void run() {

        while (patreonAccess.isEnabled()) {
            try (RedisConnection connection = RedisData.getConnection()) {
                connection.subscribe(this, patreonAccess.getLocales().getString(Config.REDIS_CHANNEL.getPath()));
            } catch (Exception exception) {
                patreonAccess.getLogger().warning("Subscriber crashato! Tento il riavvio tra " + (DELAY == 1 ? "un secondo" : DELAY + " secondi") + "...");
            }

            try {
                Thread.sleep(DELAY * 1000);
                patreonAccess.getLogger().warning("Riavvio del subscriber in corso...");
            } catch (InterruptedException e) {
                e.printStackTrace();
                patreonAccess.getLogger().severe("Impossibile riavviare il subscriber!");
            }
        }
    }
}
