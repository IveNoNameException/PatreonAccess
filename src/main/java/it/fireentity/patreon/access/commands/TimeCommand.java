package it.fireentity.patreon.access.commands;

import it.fireentity.library.command.argument.Command;
import it.fireentity.library.command.nodes.CommandNode;
import it.fireentity.library.command.row.CommandRow;
import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.commands.arguments.PlayerArgument;
import it.fireentity.patreon.access.entities.PatreonPlayer;
import it.fireentity.patreon.access.enumerations.Config;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

public class TimeCommand extends Command {
    private final PatreonAccess patreonAccess;

    public TimeCommand(PatreonAccess patreonAccess, CommandNode mainCommandNode) {
        super(patreonAccess, "time", false, mainCommandNode);
        this.addArgument(new PlayerArgument(patreonAccess, false));
        this.patreonAccess = patreonAccess;
        this.addMessage(getPath() + ".patreon_player_not_found");
        this.addMessage(getPath() + ".patreon_online_time","The time passed of the patreon");
    }

    @Override
    public void execute(CommandSender commandSender, List<String> list, CommandRow commandRow) {
        Player player = (Player) commandSender;
        Optional<PatreonPlayer> patreonPlayer = patreonAccess.getPatreonPlayerCache().getPlayer((Player) commandSender);
        if (!patreonPlayer.isPresent()) {
            getPlugin().getLocales().sendMessage(getPath() + ".patreon_player_not_found", commandSender);
            return;
        }

        if (patreonPlayer.get().getOnlineTime().isPresent()) {
            getPlugin().getLocales().sendMessage(getPath() + ".patreon_online_time", commandSender, patreonPlayer.get().getOnlineTime().get());
            return;
        }

        if(patreonPlayer.get().isExceeded()) {
            patreonAccess.getLocales().sendMessage(Config.PATREON_TIME_EXPIRED_STRING.getPath(),player);
        }
    }
}
