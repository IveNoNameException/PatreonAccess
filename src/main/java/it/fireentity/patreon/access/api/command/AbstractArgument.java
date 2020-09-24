package it.fireentity.patreon.access.api.command;

import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractArgument {
    /**
     * The flag of the argument
     */
    private final String flag;

    /**
     * The name using to identify this argument
     */
    @Getter private final String argumentName;

    /**
     * The result of the specified argument
     */
    private Object object = null;

    /**
     * The list of conflict flags
     */
    @Getter private final List<String> conflictFlags = new ArrayList<>();

    @Getter private final boolean isOptional;

    private boolean hasBeenEvaluated;

    @Getter private final int argumentsToEvaluate;

    /**
     * Constructor to specify the command flag
     * @param flag The flag of the command argument
     */
    public AbstractArgument(String flag, String argumentName, boolean isOptional, int argumentsToEvaluate) {
        this.hasBeenEvaluated = false;
        this.flag = flag;
        this.argumentName = argumentName;
        this.isOptional = isOptional;
        this.argumentsToEvaluate = argumentsToEvaluate;
    }

    /**
     * Constructor to specify no flag
     */
    public AbstractArgument(String argumentName, boolean isOptional, int argumentsToEvaluate) {
        this.hasBeenEvaluated = false;
        this.flag = null;
        this.argumentName = argumentName;
        this.isOptional = isOptional;
        this.argumentsToEvaluate = argumentsToEvaluate;
    }

    public boolean hasBeenEvaluated() {
        return hasBeenEvaluated;
    }

    /**
     * Evaluate the argument parsing the raw into an object
     * @param sender the command sender
     * @param currentArgument the argument
     * @param abstractArgumentIterator the entire argument raw
     */
    public void eval(CommandSender sender, String currentArgument, List<String> abstractArgumentIterator) throws CommandArgumentException{
        this.hasBeenEvaluated = true;
        this.object = parse(sender, currentArgument, abstractArgumentIterator);
    }

    /**
     * Check if the argument is specified into the command
     * @return returns true if the argument is specified
     */
    public boolean isSpecified() {
        return object != null;
    }

    /**
     * Reset the parsed object after the command is executed
     */
    public void reset() {
        this.hasBeenEvaluated = false;
        object = null;
    }

    public Optional<String> getFlag() {
        return Optional.ofNullable(this.flag);
    }

    /**
     * Parse the argument into an object
     * @param commandSender the command sender
     * @param currentArgument the current argument
     * @param abstractArgumentIterator the entire argument raw
     * @return returns the parsed object
     * @throws CommandArgumentException Throws is the argument is not parsable using this argument
     */
    public abstract Object parse(CommandSender commandSender, String currentArgument, List<String> abstractArgumentIterator) throws CommandArgumentException;

    /**
     * Returns an object casted the parametrized type
     * @param <T> The type of the parsed object
     * @return Returns an optional containing the parsed object
     */
    public <T> Optional<T> get() {
        return Optional.ofNullable((T) object);
    }

    /**
     * Add a new flag that makes conflict whit the current argument
     * @param flag the flag
     * @return returns the current instance to make a chain of methods
     */
    public AbstractArgument addConflictFlag(String flag) {
        this.conflictFlags.add(flag);
        return this;
    }

    public AbstractArgument addConflictFlags(String ... flags) {
        this.conflictFlags.addAll(Arrays.asList(flags));
        return this;
    }
}
