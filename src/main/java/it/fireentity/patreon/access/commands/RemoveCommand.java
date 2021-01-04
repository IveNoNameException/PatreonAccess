package it.fireentity.patreon.access.commands;

import it.fireentity.library.command.argument.Command;
import it.fireentity.library.command.nodes.CommandNode;
import it.fireentity.library.command.row.CommandRow;
import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.commands.arguments.PlayerArgument;
import it.fireentity.patreon.access.entities.PatreonPlayer;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class RemoveCommand extends Command {
    private final PatreonAccess patreonAccess;

    public RemoveCommand(PatreonAccess patreonAccess, CommandNode mainCommandNode) {
        super(patreonAccess, "remove", false, mainCommandNode);
        this.addArgument(new PlayerArgument(patreonAccess, false));
        this.addMessage(getPath() + ".player_already_removed");
        this.addMessage(getSuccessPath());
        this.patreonAccess = patreonAccess;

    }

    @Override
    public void execute(CommandSender commandSender, List<String> list, CommandRow commandRow) {
        AtomicReference<Optional<PatreonPlayer>> patreonPlayer = new AtomicReference<>(Optional.empty());
        if (commandRow.isSpecified("player")) {
            patreonPlayer.set(commandRow.getOne("player"));
        }

        if (!patreonPlayer.get().isPresent()) {
            patreonAccess.getServer().getScheduler().runTaskAsynchronously(patreonAccess, () -> {
                patreonPlayer.set(patreonAccess.getPatreonPlayersDatabaseUtility().select(list.get(0)));

                //Check if the player is present
                if(!patreonPlayer.get().isPresent()) {
                    return;
                }

                removePlayer(patreonPlayer.get().get());
            });
            return;
        }

        if (!patreonPlayer.get().isPresent()) {
            return;
        }
        removePlayer(patreonPlayer.get().get());
        getPlugin().getLocales().sendMessage(getSuccessPath(), commandSender);

    }

    private void removePlayer(PatreonPlayer patreonPlayer) {
        patreonAccess.getServer().getScheduler().runTask(patreonAccess, ()-> {
            patreonAccess.getPatreonPlayerCache().removePlayer(patreonPlayer);
            patreonAccess.getServer().getScheduler().runTaskAsynchronously(patreonAccess,
                    () -> patreonAccess.getPatreonPlayersDatabaseUtility().remove(patreonPlayer)
            );
        });
    }
}
