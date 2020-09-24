package it.fireentity.patreon.access.commands;

import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.api.command.ArgumentRaw;
import it.fireentity.patreon.access.api.command.PluginCommand;
import it.fireentity.patreon.access.entities.player.PatreonPlayer;
import it.fireentity.patreon.access.enumerations.Config;
import it.fireentity.patreon.access.enumerations.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class TimeCommand extends PluginCommand {

    public TimeCommand(PatreonAccess patreonAccess) {
        super("time", false, Permission.TIME_COMMAND_PERMISSION.getPermission(), patreonAccess);
    }

    @Override
    public void asyncExecute(CommandSender commandSender, List<String> args, ArgumentRaw argumentRaw) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(Config.INVALID_COMMAND_SENDER.getMessage());
            return;
        }

        Optional<PatreonPlayer> patreonPlayer = getPatreonAccess().getPatreonPlayerCache().getPlayer((Player) commandSender);
        if(!patreonPlayer.isPresent()) {
            commandSender.sendMessage(Config.PATREON_PLAYER_NOT_FOUND.getMessage());
        }
        patreonPlayer.ifPresent(player -> commandSender.sendMessage(Config.PATREON_ONLINE_TIME.getMessage().replace("%time", player.getOnlineTime())));
    }
}
