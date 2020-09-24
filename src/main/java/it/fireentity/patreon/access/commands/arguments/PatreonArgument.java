package it.fireentity.patreon.access.commands.arguments;

import it.fireentity.patreon.access.api.command.AbstractArgument;
import it.fireentity.patreon.access.api.command.CommandArgumentException;
import it.fireentity.patreon.access.cache.PatreonVipCache;
import it.fireentity.patreon.access.entities.vip.PatreonVip;
import it.fireentity.patreon.access.enumerations.Config;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Optional;

public class PatreonArgument extends AbstractArgument {
    private final PatreonVipCache patreonVipCache;

    public PatreonArgument(PatreonVipCache patreonVipCache) {
        super("patreon", false, 1);
        this.patreonVipCache = patreonVipCache;
    }

    @Override
    public Object parse(CommandSender commandSender, String currentArgument, List<String> abstractArgumentIterator) throws CommandArgumentException {
        if(abstractArgumentIterator.size()!=1) {
            throw new CommandArgumentException(Config.PATREON_NOT_FOUND.getMessage());
        }

        Optional<PatreonVip> patreonVip = patreonVipCache.getPatreonVip(abstractArgumentIterator.get(0));
        if(patreonVip.isPresent()) {
            return patreonVip.get();
        }

        throw new CommandArgumentException(Config.PATREON_NOT_FOUND.getMessage());

    }
}
