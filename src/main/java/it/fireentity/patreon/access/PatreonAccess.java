package it.fireentity.patreon.access;

import it.fireentity.patreon.access.api.command.GenericArgument;
import it.fireentity.patreon.access.cache.PatreonPlayerCache;
import it.fireentity.patreon.access.cache.PatreonVipCache;
import it.fireentity.patreon.access.commands.AddCommand;
import it.fireentity.patreon.access.commands.PatreonCommand;
import it.fireentity.patreon.access.commands.RemoveCommand;
import it.fireentity.patreon.access.commands.TimeCommand;
import it.fireentity.patreon.access.commands.arguments.PatreonArgument;
import it.fireentity.patreon.access.storage.mysql.WhitelistDatabaseUtility;
import it.fireentity.patreon.access.enumerations.Config;
import it.fireentity.patreon.access.enumerations.Patreon;
import it.fireentity.patreon.access.enumerations.Permission;
import it.fireentity.patreon.access.entities.whitelist.Whitelist;
import it.fireentity.patreon.access.utils.PluginFile;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class PatreonAccess extends JavaPlugin {
    private PatreonPlayerCache patreonPlayerCache;
    private PatreonVipCache patreonVipCache;
    private YamlConfiguration patreonConfig;
    private YamlConfiguration permissionsConfig;
    private YamlConfiguration config;
    private PluginFile whitelistFile;
    private WhitelistDatabaseUtility whitelistDatabaseUtility;
    private Whitelist whitelist;

    @Override
    public void onEnable() {
        initializeConfig();
        initializeEnumerations();
        initializeStorageUtility();
        initializeWhitelist();
        initializeCache();
        initializeCommands();
    }

    private void initializeWhitelist() {
        whitelist = new Whitelist(whitelistFile, whitelistDatabaseUtility);
    }

    private void initializeStorageUtility() {
        whitelistDatabaseUtility = new WhitelistDatabaseUtility(this);
    }

    private void initializeCache() {
        patreonPlayerCache = new PatreonPlayerCache(whitelist,this, whitelistDatabaseUtility);
        patreonVipCache = new PatreonVipCache(patreonConfig, whitelistDatabaseUtility.getPatreonTypesDatabaseUtility());
    }

    private void initializeCommands() {
        PatreonCommand patreonCommand = new PatreonCommand(this);

        //Register add command
        AddCommand addCommand = new AddCommand(this);
        addCommand.arguments(new PatreonArgument(patreonVipCache), new GenericArgument<>("player", false, 1, (args) -> args.get(0)));
        patreonCommand.register(addCommand);

        //Register remove command
        RemoveCommand removeCommand = new RemoveCommand(this);
        removeCommand.arguments(new GenericArgument<>("player", false, 1, (args) -> args.get(0)));
        patreonCommand.register(removeCommand);

        //Register time command
        patreonCommand.register(new TimeCommand(this));
        this.getCommand("patreon").setExecutor(patreonCommand);
    }

    private void initializeConfig() {
        PluginFile patreonFile = new PluginFile("patreonConfig", this);
        PluginFile configFile = new PluginFile("config", this);
        PluginFile permissionsFile = new PluginFile("permission", this);
        whitelistFile = new PluginFile("whitelist", this);
        permissionsFile.saveDefault();
        patreonFile.saveDefault();
        configFile.saveDefault();
        this.permissionsConfig = permissionsFile.getConfig();
        this.patreonConfig = patreonFile.getConfig();
        this.config = configFile.getConfig();
    }

    private void initializeEnumerations() {
        Permission.setConfiguration(permissionsConfig);
        Config.setConfiguration(config);
        Patreon.setConfiguration(patreonConfig);
    }
}
