package it.fireentity.patreon.access.commands;

import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.api.command.ArgumentRaw;
import it.fireentity.patreon.access.api.command.RootCommand;
import it.fireentity.patreon.access.enumerations.Config;
import it.fireentity.patreon.access.enumerations.Permission;
import org.bukkit.command.CommandSender;

import java.util.List;

public class PatreonCommand extends RootCommand {
    public PatreonCommand(PatreonAccess patreonAccess) {
        super("patreon", false, Permission.PATREON_MAIN_COMMAND_PERMISSION.getPermission(), patreonAccess, false);
    }

    @Override
    public void asyncExecute(CommandSender commandSender, List<String> args, ArgumentRaw argumentRaw) {
        if(args.size() == 0 && isForceAtLeastOneArg() && commandSender.hasPermission(Permission.PATREON_ACCESS_TIP_PERMISSION.getPermission())) {
            for(String line : Config.PATREON_USAGE.getMessageList()) {
                commandSender.sendMessage(line);
            }
        }
    }
}
