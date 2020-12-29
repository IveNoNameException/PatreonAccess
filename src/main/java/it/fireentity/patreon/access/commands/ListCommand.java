package it.fireentity.patreon.access.commands;

import it.fireentity.library.command.argument.Command;
import it.fireentity.library.command.nodes.CommandNode;
import it.fireentity.library.command.row.CommandRow;
import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.entities.PatreonPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ListCommand extends Command {
    private final PatreonAccess patreonAccess;

    public ListCommand(PatreonAccess patreonAccess,CommandNode mainCommandNode) {
        super(patreonAccess, patreonAccess.getPageTexture().clone(), "list",false,mainCommandNode);
        this.addMessage(getSuccessPath(),"The player name","Passed online time","Type of the patreon","Patreon max online time");
        this.patreonAccess = patreonAccess;
    }

    @Override
    public void execute(CommandSender commandSender, List<String> list, CommandRow commandRow) {
        Player player = (Player) commandSender;
        List<TextComponent> lines = new ArrayList<>();
        for(PatreonPlayer patreonPlayer : patreonAccess.getPatreonPlayerCache().getPlayers()) {
            if(patreonPlayer.getOnlineTime().isPresent()) {
                lines.add(new TextComponent(getPlugin().getLocales().getString(getSuccessPath(),
                        patreonPlayer.getPlayerName(),
                        patreonPlayer.getOnlineTime().get(),
                        patreonPlayer.getPatreonVip().getKey(),
                        patreonPlayer.getPatreonVip().getOnlineTime())));
            }
        }

        getPagesGroup().ifPresent(pagesGroup -> {
            pagesGroup.setLines(lines);
            pagesGroup.send(player,1);
        });
    }
}
