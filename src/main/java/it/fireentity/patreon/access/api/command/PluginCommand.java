package it.fireentity.patreon.access.api.command;

import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.enumerations.Config;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class PluginCommand {
    @Getter
    protected String label;
    protected final boolean allowedConsole;
    @Getter protected final String permission;
    @Getter private final PatreonAccess patreonAccess;
    private ArgumentRaw argumentRaw = null;

    public PluginCommand(String label, boolean allowedConsole, String permission, PatreonAccess patreonAccess) {
        this.patreonAccess = patreonAccess;
        this.label = label;
        this.allowedConsole = allowedConsole;
        this.permission = permission;
    }

    public PluginCommand arguments(AbstractArgument ... abstractArguments) {
        ArgumentRaw argumentRaw = new ArgumentRaw();
        for (AbstractArgument abstractArgument : abstractArguments) {
            argumentRaw.addArgument(abstractArgument);
        }
        this.argumentRaw = argumentRaw;
        return this;
    }

    public void onCommand(CommandSender sender, List<String> args) {
        //Check if the permission is null
        if(this.permission == null) {
            System.out.println(ChatColor.RED  + "The current command permission is null: " + label);
            return;
        }

        //Check if the player has permission
        if(!sender.hasPermission(this.permission)) {
            sender.sendMessage(Config.INSUFFICIENT_PERMISSIONS.getMessage());
            return;
        }
        if(argumentRaw != null) {
              if(!argumentRaw.evalRaw(sender, args)) {
                  return;
              }
        } else {
            this.argumentRaw = new ArgumentRaw();
        }

        asyncExecute(sender, args, this.argumentRaw);
        argumentRaw.resetAll();
    }

    public abstract void asyncExecute(CommandSender commandSender, List<String> args, ArgumentRaw argumentRaw);
}