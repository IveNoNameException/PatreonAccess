package it.fireentity.patreon.access.commands;

import it.fireentity.library.command.argument.Command;
import it.fireentity.library.command.argument.implementations.GenericArgument;
import it.fireentity.library.command.nodes.CommandNode;
import it.fireentity.library.command.row.CommandRow;
import it.fireentity.library.player.CustomPlayer;
import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.cache.PatreonVipCache;
import it.fireentity.patreon.access.commands.arguments.PatreonArgument;
import it.fireentity.patreon.access.entities.PatreonPlayer;
import it.fireentity.patreon.access.entities.PatreonVip;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class AddCommand extends Command {
    private final PatreonAccess patreonAccess;

    public AddCommand(PatreonAccess patreonAccess, PatreonVipCache patreonVipCache, CommandNode mainNode) {
        super(patreonAccess,"add",false,mainNode);
        this.addArgument(new PatreonArgument(patreonAccess,patreonVipCache,false)).addArgument(new GenericArgument<>("player", false, 1, (args) -> args.get(0)));
        this.patreonAccess = patreonAccess;
        this.addMessage(getPath() + ".player_already_added","");
    }

    @Override
    public void execute(CommandSender commandSender, List<String> list, CommandRow commandRow) {

        Optional<String> player = Optional.empty();
        if(commandRow.isSpecified("player"))  {
            player = commandRow.getOne("player");
        }

        if(!player.isPresent()) {
            return;
        }

        Optional<PatreonVip> patreonVip = Optional.empty();
        if(commandRow.isSpecified("patreon")) {
            patreonVip = commandRow.getOne("patreon");
        }

        if(!patreonVip.isPresent()) {
            return;
        }

        Optional<String> finalPlayer = player;
        Optional<PatreonVip> finalPatreonVip = patreonVip;

        //Check if the player is already present into the cache
        Optional<PatreonPlayer> patreonPlayer = patreonAccess.getPatreonPlayerCache().getPlayer(player.get());
        if(!patreonPlayer.isPresent()) {
            patreonAccess.getServer().getScheduler().runTaskAsynchronously(patreonAccess, () -> {
                patreonAccess.getPatreonPlayersDatabaseUtility().insert(new PatreonPlayer(finalPatreonVip.get(),finalPlayer.get(),0));
            });
            getPlugin().getLocales().sendMessage(getSuccessPath(), commandSender);
        } else {
            getPlugin().getLocales().sendMessage(getPath() + ".player_already_added",commandSender);
        }
    }
}
