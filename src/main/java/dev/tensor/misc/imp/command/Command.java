package dev.tensor.misc.imp.command;

import dev.tensor.misc.imp.Globals;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

public interface Command extends Globals {

    String getName();

    String[] getAcceptedNames();

    String getRegex();

    String getDescription();

    void onCommand(String[] ARGS);

    default String[] getRegexInArray() {
        return this.getRegex().split(" ");
    }
}
