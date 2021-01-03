package it.fireentity.patreon.access.commands.arguments;

import it.fireentity.library.command.argument.AbstractArgument;
import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.cache.PatreonVipCache;
import it.fireentity.patreon.access.entities.PatreonVip;
import it.fireentity.patreon.access.enumerations.Config;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.*;

public class PatreonArgument extends AbstractArgument {
    private final PatreonVipCache patreonVipCache;
    private static PatreonAccess patreonAccess;

    public PatreonArgument(PatreonAccess patreonAccess, PatreonVipCache patreonVipCache, boolean isOptional) {
        super("patreon", isOptional, 1);
        this.patreonVipCache = patreonVipCache;
        PatreonArgument.patreonAccess = patreonAccess;
    }

    @Override
    public Collection<TextComponent> getPossibleValues() {
        List<TextComponent> lines = new ArrayList<>();
        for(PatreonVip patreonVip : patreonVipCache.getPatreonList()) {
            lines.add( new TextComponent(patreonAccess.getLocales().getString(Config.PATREON_VIP_LINE.getPath(), patreonVip.getPatreonDisplayName(), patreonVip.getOnlineTime())));
        }
        return lines;
    }

    @Override
    public Object parseForConsoleSender(String s, List<String> list) {
        if(list.size()!=1) {
            return null;
        }

        Optional<PatreonVip> patreonVip = patreonVipCache.getPatreonVip(list.get(0));
        return patreonVip.orElse(null);
    }

    @Override
    public Object parseForPlayerSender(String s, String s1, List<String> list) {
        if(list.size()!=1) {
            return null;
        }

        Optional<PatreonVip> patreonVip = patreonVipCache.getPatreonVip(list.get(0));
        return patreonVip.orElse(null);
    }
}
