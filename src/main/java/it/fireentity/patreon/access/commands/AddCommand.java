package it.fireentity.patreon.access.commands;

import it.fireentity.library.command.argument.Command;
import it.fireentity.library.command.nodes.CommandNode;
import it.fireentity.library.command.row.CommandRow;
import it.fireentity.library.player.CustomPlayer;
import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.cache.PatreonVipCache;
import it.fireentity.patreon.access.commands.arguments.PatreonArgument;
import it.fireentity.patreon.access.commands.arguments.PlayerArgument;
import it.fireentity.patreon.access.entities.PatreonVip;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Optional;

public class AddCommand extends Command {
    private final PatreonAccess patreonAccess;

    public AddCommand(PatreonAccess patreonAccess, PatreonVipCache patreonVipCache, CommandNode mainNode) {
        super(patreonAccess,"add",false,mainNode);
        this.addArgument(new PatreonArgument(patreonAccess,patreonVipCache,false)).addArgument(new PlayerArgument(patreonAccess,false));
        this.patreonAccess = patreonAccess;
        this.addMessage(getPath() + ".player_already_added","");
    }

    @Override
    public void execute(CommandSender commandSender, List<String> list, CommandRow commandRow) {

        Optional<CustomPlayer> customPlayer = Optional.empty();
        if(commandRow.isSpecified("player")) {
            customPlayer = commandRow.getOne("player");
        }

        if(!customPlayer.isPresent()) {
            return;
        }

        Optional<PatreonVip> patreonVip = Optional.empty();
        if(commandRow.isSpecified("patreon")) {
            patreonVip = commandRow.getOne("patreon");
        }

        if(!patreonVip.isPresent()) {
            return;
        }

        //Check if the player is already whitelisted
        if(patreonAccess.getWhitelist().getWhitelistedPlayers().contains(customPlayer.get().getKey())) {
            getPlugin().getLocales().sendMessage(getPath() + ".player_already_added",commandSender);
            return;
        }

        getPlugin().getLocales().sendMessage(getSuccessPath(), commandSender);
        Optional<CustomPlayer> finalCustomPlayer = customPlayer;
        Optional<PatreonVip> finalPatreonVip = patreonVip;
        Bukkit.getScheduler().runTaskAsynchronously(this.getPlugin(), () -> {
            patreonAccess.getWhitelist().addPlayer(finalCustomPlayer.get().getKey());
            patreonAccess.getWhitelist().addPlayerWithDatabase(finalPatreonVip.get(),finalCustomPlayer.get().getKey());
        });
    }
}
