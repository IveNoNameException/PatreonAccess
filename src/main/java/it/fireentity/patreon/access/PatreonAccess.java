package it.fireentity.patreon.access;

import it.fireentity.library.AbstractPlugin;
import it.fireentity.library.chatpaging.PageTexture;
import it.fireentity.library.command.nodes.CommandNode;
import it.fireentity.library.locales.Message;
import it.fireentity.library.utils.PluginFile;
import it.fireentity.patreon.access.cache.PatreonPlayerCache;
import it.fireentity.patreon.access.cache.PatreonVipCache;
import it.fireentity.patreon.access.commands.AddCommand;
import it.fireentity.patreon.access.commands.ListCommand;
import it.fireentity.patreon.access.commands.RemoveCommand;
import it.fireentity.patreon.access.commands.TimeCommand;
import it.fireentity.patreon.access.enumerations.Config;
import it.fireentity.patreon.access.storage.mysql.PatreonPlayersDatabaseUtility;
import it.fireentity.patreon.access.storage.mysql.PatreonVipsDatabaseUtility;
import it.fireentity.patreon.access.storage.mysql.PlayerDatabaseUtility;
import it.fireentity.patreon.access.storage.redis.RedisPubSub;
import lombok.Getter;

import java.util.Optional;

@Getter
public class PatreonAccess extends AbstractPlugin {
    private PatreonPlayerCache patreonPlayerCache;
    private PatreonVipCache patreonVipCache;
    private PatreonVipsDatabaseUtility patreonVipsDatabaseUtility;
    private PlayerDatabaseUtility playerDatabaseUtility;
    private PatreonPlayersDatabaseUtility  patreonPlayersDatabaseUtility;
    private PluginFile patreonFile;


    @Override
    public CommandNode initializeMainCommandNode() {
        return new CommandNode(this,"patreon",false);
    }

    @Override
    public void onStart() {

        patreonFile = new PluginFile("patreon",this);
        patreonFile.saveDefault();

        patreonVipsDatabaseUtility = new PatreonVipsDatabaseUtility();
        playerDatabaseUtility = new PlayerDatabaseUtility();
        patreonPlayersDatabaseUtility = new PatreonPlayersDatabaseUtility(this);

        Thread thread = new Thread(new RedisPubSub(this));
        thread.start();

        patreonPlayerCache = new PatreonPlayerCache(this);
        patreonVipCache = new PatreonVipCache(this, patreonFile, patreonVipsDatabaseUtility);

        Optional<PageTexture> pageTextureOptional = getPageTexture();
        if(!pageTextureOptional.isPresent()) {
            return;
        }
        PageTexture pageTexture = pageTextureOptional.get();

        //Register add command
        AddCommand addCommand = new AddCommand(this, patreonVipCache, getMainNode());
        RemoveCommand removeCommand = new RemoveCommand(this, getMainNode());
        ListCommand listCommand = new ListCommand(this, pageTexture,getMainNode());
        TimeCommand timeCommand = new TimeCommand(this, getMainNode());

        for (Config path : Config.values()) {
            Message message = new Message(path.getPath());
            if (path.getPath().equals(Config.PATREON_VIP_LINE.getPath())) {
                getLocales().addMessage(message,"Display name of the patreon type", "Max online time");
            } else {
                getLocales().addMessage(message);
            }
        }
    }

    @Override
    protected Optional<PageTexture> initializePageTexture() {
        Optional<Integer> maxPageSize = getLocales().getInteger(it.fireentity.library.enumerations.Config.CHAT_PAGINATION_MAX_PAGE_SIZE.getPath());
        String nextButton = getLocales().getString(it.fireentity.library.enumerations.Config.CHAT_PAGINATION_NEXT_BUTTON.getPath());
        String backButton = getLocales().getString(it.fireentity.library.enumerations.Config.CHAT_PAGINATION_BACK_BUTTON.getPath());
        String endMessage = getLocales().getString(it.fireentity.library.enumerations.Config.CHAT_PAGINATION_START_MESSAGE.getPath());
        String startMessage = getLocales().getString(it.fireentity.library.enumerations.Config.CHAT_PAGINATION_START_MESSAGE.getPath());
        String noLineFound = getLocales().getString(it.fireentity.library.enumerations.Config.CHAT_PAGINATION_NO_LINE_FOUND.getPath());
        String onePageStartMessage = getLocales().getString(it.fireentity.library.enumerations.Config.ONE_PAGE_START_MESSAGE.getPath());
        String onePageEndMessage = getLocales().getString(it.fireentity.library.enumerations.Config.ONE_PAGE_END_MESSAGE.getPath());

        return maxPageSize.map(integer -> new PageTexture(nextButton, backButton, endMessage, startMessage, noLineFound, integer, onePageStartMessage, onePageEndMessage));
    }
}
