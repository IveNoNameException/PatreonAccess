package it.fireentity.patreon.access.cache;

import it.fireentity.patreon.access.entities.vip.PatreonVip;
import it.fireentity.patreon.access.enumerations.Patreon;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;

@Getter
public class PatreonVipCache {
    private final HashMap<String, PatreonVip> patreonVipList = new HashMap<>();

    public PatreonVipCache(YamlConfiguration patreon) {
        for(String patreonName : patreon.getKeys(false)) {
            String patreonDisplayName = Patreon.PATREON_NAME.getMessage();
            long patreonMaxOnlineTime = Patreon.PATREON_TIME.getLong();
            PatreonVip patreonVip = new PatreonVip(patreonName, patreonDisplayName, patreonMaxOnlineTime);
            patreonVipList.put(patreonVip.getPatreonName(), patreonVip);
        }
    }
}
