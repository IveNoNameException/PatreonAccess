package it.fireentity.patreon.access.api.command;

import java.util.List;

public interface Action<T> {
    T action(List<String> argumentsToEvaluate);
}
