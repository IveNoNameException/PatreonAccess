package it.fireentity.patreon.access;

import it.fireentity.library.AbstractPlugin;
import it.fireentity.library.command.argument.Command;
import it.fireentity.library.command.nodes.CommandNode;
import it.fireentity.library.locales.Message;
import it.fireentity.library.utils.PluginFile;
import it.fireentity.patreon.access.cache.PatreonPlayerCache;
import it.fireentity.patreon.access.cache.PatreonVipCache;
import it.fireentity.patreon.access.commands.*;
import it.fireentity.patreon.access.storage.mysql.PatreonVipsDatabaseUtility;
import it.fireentity.patreon.access.storage.mysql.WhitelistDatabaseUtility;
import it.fireentity.patreon.access.enumerations.Config;
import it.fireentity.patreon.access.entities.Whitelist;
import it.fireentity.patreon.access.storage.redis.RedisPubSub;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class PatreonAccess extends AbstractPlugin {
    private PatreonPlayerCache patreonPlayerCache;
    private PatreonVipCache patreonVipCache;
    private WhitelistDatabaseUtility whitelistDatabaseUtility;
    private PatreonVipsDatabaseUtility patreonVipsDatabaseUtility;
    private Whitelist whitelist;


    @Override
    protected void onStart() {
        whitelist = new Whitelist(this,whitelistDatabaseUtility);
        Thread thread = new Thread(new RedisPubSub(this));
        thread.start();
    }

    @Override
    protected List<Message> initializeMessageList() {
        List<Message> messages = new ArrayList<>();
        for(Config path : Config.values()) {
            Message message = new Message(path.getPath());
            messages.add(new Message(path.getPath()));
            if(path.getPath().equals(Config.PATREON_VIP_LINE.getPath())) {
                message.addArguments("Display name of the patreon type", "Max online time");
            }
        }
        return messages;
    }

    @Override
    public List<Command> initializeCommands() {
        List<Command> commands = new ArrayList<>();

        //Register add command
        AddCommand addCommand = new AddCommand(this,patreonVipCache,getMainNode());
        RemoveCommand removeCommand = new RemoveCommand(this,getMainNode());
        ListCommand listCommand = new ListCommand(this, getMainNode());
        TimeCommand timeCommand = new TimeCommand(this,getMainNode());
        commands.add(addCommand);
        commands.add(removeCommand);
        commands.add(listCommand);
        commands.add(timeCommand);
        this.getCommand("patreon").setExecutor(getMainNode());
        return commands;
    }

    @Override
    protected List<it.fireentity.library.utils.PluginFile> initializeConfigs() {
        List<PluginFile> files = new ArrayList<>();
        files.add(new PluginFile("patreonConfig", this));
        files.add(new PluginFile("whitelist", this));
        return files;
    }

    @Override
    protected void initializeCaches() {
        patreonPlayerCache = new PatreonPlayerCache(whitelist,this, whitelistDatabaseUtility);
        this.getPluginFileCache().getValue("patreon").ifPresent(file -> patreonVipCache = new PatreonVipCache(this,file,patreonVipsDatabaseUtility));
    }

    @Override
    protected void initializeDatabaseUtility() {
        whitelistDatabaseUtility = new WhitelistDatabaseUtility(this);
        patreonVipsDatabaseUtility = new PatreonVipsDatabaseUtility();
    }

    @Override
    protected void initializeListeners() {

    }

    @Override
    protected Optional<CommandNode> initializeMainNode() {
        return Optional.of(new CommandNode(this,"patreon",false));
    }

    private void initializeThread() {
        Thread thread = new Thread(new RedisPubSub(this));
        thread.start();
    }
}
