package it.fireentity.patreon.access.cache;

import it.fireentity.patreon.access.entities.vip.PatreonVip;
import it.fireentity.patreon.access.storage.mysql.PatreonTypesDatabaseUtility;
import it.fireentity.patreon.access.enumerations.Patreon;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;

@Getter
public class PatreonVipCache {
    private final HashMap<String, PatreonVip> patreonVipList = new HashMap<>();

    public PatreonVipCache(YamlConfiguration patreon, PatreonTypesDatabaseUtility patreonTypesDatabaseUtility) {

        List<String> missingConfigMessages = new ArrayList<>();
        List<String> missingDatabaseMessages = new ArrayList<>();
        List<String> databaseMessages = patreonTypesDatabaseUtility.loadPatreonVips();
        Set<String> configPatreons = patreon.getKeys(false);

        //Check for differences between config and database
        for (String patreonName : configPatreons) {
            //Check if the message is into the config and not into the database
            if (configPatreons.contains(patreonName) && !databaseMessages.contains(patreonName)) {
                missingDatabaseMessages.add(patreonName);
            }
        }

        for (String patreonName : databaseMessages) {
            //Check if the message is into the database and not into the config
            if (!configPatreons.contains(patreonName) && databaseMessages.contains(patreonName)) {
                missingConfigMessages.add(patreonName);
            }
        }

        //Adding missing messages into the database
        for (String patreonName : missingDatabaseMessages) {
            patreonTypesDatabaseUtility.insert(patreonName);
        }

        //Removing messages names from the database
        for (String patreonName : missingConfigMessages) {
            //Removing player active message
            patreonTypesDatabaseUtility.remove(patreonName);
        }

        for (String patreonName : patreon.getKeys(false)) {
            String patreonDisplayName = Patreon.PATREON_NAME.setPlaceholderAndReset("%patreon", patreonName).getMessage();
            long patreonMaxOnlineTime = Patreon.PATREON_TIME.setPlaceholderAndReset("%patreon", patreonName).getLong();
            PatreonVip patreonVip = new PatreonVip(patreonName, patreonDisplayName, patreonMaxOnlineTime);
            patreonVipList.put(patreonVip.getPatreonName(), patreonVip);
        }
    }

    public Optional<PatreonVip> getPatreonVip(String patreonName) {
        return Optional.ofNullable(patreonVipList.get(patreonName));
    }
}
