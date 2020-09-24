package it.fireentity.patreon.access.api.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomIterator <T> {
    private final List<T> list;
    private int index = 0;
    private int savePoint = 0;

    public CustomIterator(List<T> arguments) {
        this.list = arguments;
    }

    public Optional<T> next() {
        if(index >= list.size()) {
            return Optional.empty();
        }
        Optional<T> object = Optional.ofNullable(list.get(index));
        index++;
        return object;
    }

    public int getCurrentIndex() {
        return index;
    }

    public Optional<T> currentArgument() {
        if(index >= list.size()) {
            return Optional.empty();
        }
        return Optional.ofNullable(list.get(index));
    }

    public List<T> getNextElementsList(int numberOfElements) {
        if(index+numberOfElements >= list.size()+1) {
            return new ArrayList<>();
        }

        return list.subList(index, index+numberOfElements);
    }

    public void goNext(int positionsNumber) {
        index += positionsNumber;
    }

    public void resetToSavePoint() {
        index = savePoint;
    }

    public void setSavePoint() {
        this.savePoint = index;
    }

    public boolean hasCurrent() {
        return list.get(index) != null;
    }
}
