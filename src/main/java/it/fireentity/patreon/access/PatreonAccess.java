package it.fireentity.patreon.access;

import it.fireentity.patreon.access.cache.PatreonPlayerCache;
import it.fireentity.patreon.access.thread.OnlineTimeChecker;
import it.fireentity.patreon.access.entities.whitelist.StorageUtility;
import it.fireentity.patreon.access.entities.whitelist.Whitelist;
import it.fireentity.patreon.access.entities.whitelist.WhitelistFileUtility;
import it.fireentity.patreon.access.utils.PluginFile;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public class PatreonAccess extends JavaPlugin {
    private PatreonPlayerCache patreonPlayerCache;
    private PluginFile whitelist;
    private StorageUtility<List<String>> storageUtility;

    @Override
    public void onEnable() {
        initializeConfig();
        initializeCache();
        initializeWhitelist();
    }

    private void initializeStorageUtility() {
        storageUtility = new WhitelistFileUtility(whitelist, "whitelist");
    }

    private void initializeThread() {
        new OnlineTimeChecker(this);
    }

    private void initializeCache() {
        patreonPlayerCache = new PatreonPlayerCache();
    }

    private void initializeConfig() {
        whitelist = new PluginFile("whitelist", this);
    }

    private void initializeWhitelist() {
        Whitelist whitelist = new Whitelist(getWhitelist(), storageUtility);
    }
}
