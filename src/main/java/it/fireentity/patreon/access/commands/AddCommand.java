package it.fireentity.patreon.access.commands;

import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.api.command.ArgumentRaw;
import it.fireentity.patreon.access.api.command.PluginCommand;
import it.fireentity.patreon.access.entities.vip.PatreonVip;
import it.fireentity.patreon.access.enumerations.Config;
import it.fireentity.patreon.access.enumerations.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Optional;

public class AddCommand extends PluginCommand {

    public AddCommand(PatreonAccess patreonAccess) {
        super("add", false, Permission.ADD_COMMAND_PERMISSION.getPermission(), patreonAccess);
    }

    @Override
    public void asyncExecute(CommandSender commandSender, List<String> args, ArgumentRaw argumentRaw) {
        if(args.size() != 2) {
            commandSender.sendMessage(Config.ADD_COMMAND_USAGE.getMessage());
            return;
        }

        String target;
        if(argumentRaw.isSpecified("player")) {
            Optional<String> player = argumentRaw.getOne("player");
            if(player.isPresent()) {
                target = player.get();
            } else {
                return;
            }
        } else {
            return;
        }

        PatreonVip patreonVip;
        if(argumentRaw.isSpecified("patreon")) {
            Optional<PatreonVip> optionalPatreonVip = argumentRaw.getOne("patreon");
            if(optionalPatreonVip.isPresent()) {
                patreonVip = optionalPatreonVip.get();
            } else {
                return;
            }
        } else {
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(getPatreonAccess(), () -> {
            if(getPatreonAccess().getWhitelist().addPlayerWithDatabase(patreonVip, target)) {
                getPatreonAccess().getPatreonPlayerCache().addPlayer(target, patreonVip.getPatreonName());
                commandSender.sendMessage(Config.ADD_COMMAND_SUCCESS.getMessage());
            } else {
                commandSender.sendMessage(Config.PLAYER_ALREADY_ADDED.getMessage());
            }
        });
    }
}
