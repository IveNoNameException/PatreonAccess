package it.fireentity.patreon.access.commands;

import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.api.command.ArgumentRaw;
import it.fireentity.patreon.access.api.command.PluginCommand;
import it.fireentity.patreon.access.entities.player.PatreonPlayer;
import it.fireentity.patreon.access.enumerations.Config;
import it.fireentity.patreon.access.enumerations.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class RemoveCommand extends PluginCommand {

    public RemoveCommand(PatreonAccess patreonAccess) {
        super("remove", false, Permission.REMOVE_COMMAND_PERMISSION.getPermission(), patreonAccess);
    }

    @Override
    public void asyncExecute(CommandSender commandSender, List<String> args, ArgumentRaw argumentRaw) {
        if (args.size() != 1) {
            commandSender.sendMessage(Config.REMOVE_COMMAND_USAGE.getMessage());
            return;
        }

        if(argumentRaw.isSpecified("player")) {
            Optional<String> player = argumentRaw.getOne("player");
            if(player.isPresent()) {
                if(!getPatreonAccess().getWhitelist().isWhitelisted(player.get())) {
                    commandSender.sendMessage(Config.PATREON_NOT_WHITELISTED.getMessage());
                    return;
                }

                Bukkit.getScheduler().runTaskAsynchronously(getPatreonAccess(), () -> {
                    getPatreonAccess().getWhitelist().removePlayer(args.get(0));
                });


                Player target = Bukkit.getPlayer(player.get());
                if(target == null) {
                    return;
                }

                Optional<PatreonPlayer> patreonPlayer = getPatreonAccess().getPatreonPlayerCache().getPlayer(target);
                if(!patreonPlayer.isPresent()) {
                    commandSender.sendMessage(Config.PATREON_NOT_WHITELISTED.getMessage());
                    return;
                } else {
                    getPatreonAccess().getPatreonPlayerCache().removePlayer(patreonPlayer.get());
                }
                commandSender.sendMessage(Config.REMOVE_COMMAND_SUCCESS.getMessage());
            }
        }
    }
}
