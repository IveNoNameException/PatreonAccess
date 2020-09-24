package it.fireentity.patreon.access.api.command;

import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.enumerations.Config;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public abstract class RootCommand extends PluginCommand implements CommandExecutor, TabExecutor {
    private final boolean forceAtLeastOneArg;
    private final List<PluginCommand> commands = new ArrayList<>();

    public RootCommand(String label, boolean allowedConsole, String permission, PatreonAccess patreonAccess, boolean forceAtLeastOneArg) {
        super(label, allowedConsole, permission, patreonAccess);
        this.forceAtLeastOneArg = forceAtLeastOneArg;
    }

    public void register(PluginCommand pluginCommand) {
        commands.add(pluginCommand);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player) && !allowedConsole) {
            //The sender is the Console. Console is not allowed to do this command;
            commandSender.sendMessage(Config.INVALID_COMMAND_SENDER.getMessage());
            return false;
        }

        List<String> args;
        if(strings.length != 0) {
            args = new ArrayList<>(Arrays.asList(strings));
        } else {
            args = new ArrayList<>();
        }

        onCommand(commandSender, args);
        fetchCommands(commandSender, command, s, strings, args);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }

    public void fetchCommands(CommandSender commandSender, Command cmd, String s, String[] strings, List<String> args) {
        //Check if the command has subarguments
        if(args.size() == 0) {
            return;
        }
        for (PluginCommand command : commands) {
            if(command.getLabel().equals(args.get(0))) {
                if(command instanceof RootCommand) {
                    ((RootCommand) command).onCommand(commandSender, cmd, s, Arrays.copyOfRange(strings, 1, strings.length));
                } else {
                    command.onCommand(commandSender, new ArrayList<>(args.subList(1, args.size())));
                }
            }
        }
    }
}