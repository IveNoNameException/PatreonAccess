package it.fireentity.patreon.access.commands.arguments;

import it.fireentity.library.command.argument.AbstractArgument;
import it.fireentity.patreon.access.PatreonAccess;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerArgument extends AbstractArgument {
    private static PatreonAccess patreonAccess;

    public PlayerArgument(PatreonAccess patreonAccess, boolean isOptional) {
        super("player", isOptional, 1);
        PlayerArgument.patreonAccess = patreonAccess;
    }

    @Override
    public Collection<TextComponent> getPossibleValues() {
        return new ArrayList<>();
    }

    @Override
    public Object parseForConsoleSender(String s, List<String> list) {
        return patreonAccess.getAPIFireLibrary().getPlayers().getPlayer(s).orElse(null);
    }

    @Override
    public Object parseForPlayerSender(String s, String s1, List<String> list) {
        return patreonAccess.getAPIFireLibrary().getPlayers().getPlayer(s).orElse(null);
    }
}
