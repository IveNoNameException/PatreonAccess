package it.fireentity.patreon.access.entities.whitelist;

import it.fireentity.patreon.access.utils.PluginFile;

import java.util.List;

public class WhitelistFileUtility implements StorageUtility<List<String>>{

    private final PluginFile whitelist;
    private final String workPath;

    public WhitelistFileUtility(PluginFile whitelist, String workPath) {
        this.whitelist = whitelist;
        this.workPath = workPath;
    }

    @Override
    public void set(List<String> strings) {
        whitelist.getConfig().set(workPath, strings);
        whitelist.save();
    }
}
