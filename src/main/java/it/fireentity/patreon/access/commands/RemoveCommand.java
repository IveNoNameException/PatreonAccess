package it.fireentity.patreon.access.commands;

import it.fireentity.library.command.argument.Command;
import it.fireentity.library.command.nodes.CommandNode;
import it.fireentity.library.command.row.CommandRow;
import it.fireentity.library.player.CustomPlayer;
import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.commands.arguments.PlayerArgument;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Optional;

public class RemoveCommand extends Command {
    private final PatreonAccess patreonAccess;

    public RemoveCommand(PatreonAccess patreonAccess, CommandNode mainCommandNode) {
        super(patreonAccess,"remove",false,mainCommandNode);
        this.addArgument(new PlayerArgument(patreonAccess, false));
        this.addMessage(getPath() + ".player_already_removed");
        this.addMessage(getSuccessPath());
        this.patreonAccess = patreonAccess;

    }

    @Override
    public void execute(CommandSender commandSender, List<String> list, CommandRow commandRow) {

        Optional<CustomPlayer> customPlayer = Optional.empty();
        if (commandRow.isSpecified("player")) {
            customPlayer = commandRow.getOne("player");
        }

        if(!customPlayer.isPresent()) {
            return;
        }

        //Check if the player is already removed
        if(patreonAccess.getWhitelist().getWhitelistedPlayers().contains(customPlayer.get().getKey())) {
            patreonAccess.getLocales().sendMessage(getPath() + ".player_already_removed",commandSender);
            return;
        }

        Optional<CustomPlayer> finalCustomPlayer = customPlayer;
        patreonAccess.getServer().getScheduler().runTaskAsynchronously(patreonAccess, () -> {
            patreonAccess.getWhitelist().removePlayer(finalCustomPlayer.get().getKey());
        });
        getPlugin().getLocales().sendMessage(getSuccessPath(), commandSender);
    }
}
