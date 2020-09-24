package it.fireentity.patreon.access.api.command;

import org.bukkit.command.CommandSender;

import java.util.*;

public class ArgumentRaw {
    private final LinkedHashMap<String, AbstractArgument> argumentsList = new LinkedHashMap<>();
    private final List<AbstractArgument> nonOptionalArguments = new ArrayList<>();

    /**
     * Add a new instance of AbstractArgument
     * @param abstractArgument the argument to add
     */
    public void addArgument(AbstractArgument abstractArgument) {
        argumentsList.put(abstractArgument.getArgumentName(), abstractArgument);

        if(!abstractArgument.isOptional()) {
            nonOptionalArguments.add(abstractArgument);
        }
    }

    /**
     * Check if the given argument
     * @param argumentName the name of the argument
     * @return returns if on instance of AbstractArgument is specified with that name
     */
    public boolean isSpecified(String argumentName) {
        if(argumentsList.get(argumentName) != null) {
            return argumentsList.get(argumentName).isSpecified();
        }
        return false;
    }

    public boolean hasArguments() {
        for(AbstractArgument argument : argumentsList.values()) {
            if(argument.get().isPresent()) {
               return true;
            }
        }
        return false;
    }

    /**
     * Get an instance of AbstractArgument with that name
     * @param argumentName the name of the argument
     * @param <T> the parametrized type
     * @return returns the instance of the argument
     */
    public <T> Optional<T> getOne(String argumentName) {
        return argumentsList.get(argumentName).get();
    }

    /**
     * Evaluate the entire command raw
     * @param sender the command sender
     * @param args the arguments of the command
     */
    public boolean evalRaw(CommandSender sender, List<String> args) {
        //Get the iterator of arguments
        CustomIterator<String> stringArguments = new CustomIterator<>(args);
        Iterator<AbstractArgument> abstractArguments = argumentsList.values().iterator();

        int totalEvaluatedArguments = 0;
        while(stringArguments.currentArgument().isPresent() && abstractArguments.hasNext()) {
            String currentArgument = stringArguments.currentArgument().get();
            AbstractArgument currentArgumentInstance = abstractArguments.next();

            if (currentArgumentInstance.getFlag().isPresent() && !currentArgumentInstance.isOptional()) {
                if (!currentArgument.equals(currentArgumentInstance.getFlag().get())) {
                    return false;
                }

                try {
                    totalEvaluatedArguments += evaluateFlaggedArgument(stringArguments, currentArgumentInstance, sender, currentArgument);
                    stringArguments.goNext(currentArgumentInstance.getArgumentsToEvaluate());
                } catch (CommandArgumentException exception) {
                    sender.sendMessage(exception.getMessage());
                    return false;
                }
            } else if (currentArgumentInstance.getFlag().isPresent() && currentArgumentInstance.isOptional()) {
                if (currentArgument.equals(currentArgumentInstance.getFlag().get())) {
                    try {
                        totalEvaluatedArguments += evaluateFlaggedArgument(stringArguments, currentArgumentInstance, sender, currentArgument);
                        stringArguments.goNext(currentArgumentInstance.getArgumentsToEvaluate());
                    } catch (CommandArgumentException ignore) {
                    }
                }
            } else {
                try {
                    totalEvaluatedArguments += evaluateArgument(stringArguments, currentArgumentInstance, sender, currentArgument);
                    stringArguments.goNext(currentArgumentInstance.getArgumentsToEvaluate());
                } catch (CommandArgumentException exception) {
                    sender.sendMessage(exception.getMessage());
                    return false;
                }
            }
        }

        if(!checkNonOptionalArguments()) {
            return false;
        }

        return args.size() == totalEvaluatedArguments;
    }

    private boolean checkNonOptionalArguments() {
        for (AbstractArgument abstractArgument : nonOptionalArguments) {
            if(!abstractArgument.hasBeenEvaluated()) {
                return false;
            }
        }

        return true;
    }

    private int evaluateFlaggedArgument(CustomIterator<String> stringArguments, AbstractArgument argument, CommandSender sender, String flag) throws CommandArgumentException{
        stringArguments.next();
        argument.eval(sender, flag, stringArguments.getNextElementsList(argument.getArgumentsToEvaluate()));

        //Increasing of 1 because of the flag
        return 1+ argument.getArgumentsToEvaluate();
    }

    private int evaluateArgument(CustomIterator<String> stringArguments, AbstractArgument argument, CommandSender sender, String flag) throws CommandArgumentException{
        argument.eval(sender, flag, stringArguments.getNextElementsList(argument.getArgumentsToEvaluate()));

        //Increasing of 1 because of the flag
        return argument.getArgumentsToEvaluate();
    }

    /**
     * Resent all arguments content
     */
    public void resetAll() {
        for(AbstractArgument abstractArgument : argumentsList.values()) {
            abstractArgument.reset();
        }
    }
}
