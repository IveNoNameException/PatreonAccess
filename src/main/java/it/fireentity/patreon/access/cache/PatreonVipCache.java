package it.fireentity.patreon.access.cache;

import it.fireentity.library.storage.Cache;
import it.fireentity.library.storage.DatabaseSynchronizer;
import it.fireentity.library.utils.PluginFile;
import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.entities.PatreonVip;
import it.fireentity.patreon.access.storage.mysql.PatreonVipsDatabaseUtility;

import java.util.*;

public class PatreonVipCache {
    private final Cache<String, PatreonVip> cache = new Cache<>();
    private final PluginFile pluginFile;

    public PatreonVipCache(PatreonAccess patreonAccess, PluginFile pluginFile, PatreonVipsDatabaseUtility patreonVipsDatabaseUtility) {
        DatabaseSynchronizer<PatreonVip, String> synchronizer = new DatabaseSynchronizer<>(patreonAccess, patreonVipsDatabaseUtility);
        this.pluginFile = pluginFile;
        List<PatreonVip> patreonVipList = initializePatreonVip();
        synchronizer.synchronize(patreonVipList);
        for(PatreonVip patreonVip : patreonVipList) {
            cache.addValue(patreonVip);
        }
    }

    public List<PatreonVip> initializePatreonVip() {
        List<PatreonVip> patreonVips = new ArrayList<>();
        for(String patreon : pluginFile.getConfig().getKeys(false)) {
            int maxOnlineTime = pluginFile.getConfig().getInt(patreon + ".max_online_time");
            String displayName = pluginFile.getConfig().getString(patreon + ".display_name");
            patreonVips.add(new PatreonVip(patreon,displayName,maxOnlineTime));
        }
        return patreonVips;
    }

    public Optional<PatreonVip> getPatreonVip(String patreonName) {
        return cache.getValue(patreonName);
    }

    public Collection<PatreonVip> getPatreonList() {
        return cache.getValues();
    }
}
