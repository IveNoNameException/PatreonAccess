package it.fireentity.patreon.access.api.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public class GenericArgument<T> extends AbstractArgument{
    private final Action action;
    public GenericArgument(String argumentName, boolean isOptional, int argumentsToEvaluate, Action<T> action) {
        super(argumentName, isOptional, argumentsToEvaluate);
        this.action = action;
    }

    @Override
    public Object parse(CommandSender commandSender, String currentArgument, List<String> abstractArgumentIterator) throws CommandArgumentException {
        return action.action(abstractArgumentIterator);
    }
}
