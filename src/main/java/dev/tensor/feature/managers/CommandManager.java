package dev.tensor.feature.managers;

import dev.tensor.backend.events.ChatSentEvent;
import dev.tensor.feature.commands.*;
import dev.tensor.feature.hacks.Commands;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.event.imp.Priority;
import dev.tensor.misc.imp.Manager;
import dev.tensor.misc.imp.command.Command;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

public final class CommandManager implements Manager {

    @Override
    public String getName() {
        return "CommandManager";
    }

    private final ArrayList<Command> COMMANDS = new ArrayList<>();

    @Override
    public void load(Logger LOGGER) {
        LOGGER.info("Loaded: " + this.getName());

        this.addCommand(new ClearChat());
        this.addCommand(new dev.tensor.feature.commands.Commands());
        this.addCommand(new Config());
        this.addCommand(new Friends());
        this.addCommand(new Hacks());
        this.addCommand(new Help());
        this.addCommand(new Options());
        this.addCommand(new Prefix());
        this.addCommand(new Reset());
        this.addCommand(new Setting());
        this.addCommand(new Toggle());

        this.postSortHacks();
    }

    public void addCommand(Command COMMAND) {
        this.COMMANDS.add(COMMAND);
    }

    public ArrayList<Command> getCommands() {
        return this.COMMANDS;
    }

    public ArrayList<Command> getCommandsThatStartWith(String text) {
        ArrayList<Command> commands = new ArrayList<>();

        this.getCommands().forEach(command -> {

            if (command.getName().toLowerCase().startsWith(text.toLowerCase())) {
                commands.add(command);
            }
        });

        return commands;
    }

    public ArrayList<Command> getListCommandsThatStartWith(String text) {
        ArrayList<Command> commands = new ArrayList<>();

        this.getCommands().forEach(command -> Arrays.stream(command.getAcceptedNames()).forEach(string -> {

            if (string.toLowerCase().startsWith(text.toLowerCase())) {
                commands.add(command);
            }
        }));

        return commands;
    }

    public Command getCommand(String name) {
        return this.getCommands().stream().filter(command -> command.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Command getListCommand(String name) {
        AtomicReference<Command> command = new AtomicReference<>(null);

        this.getCommands().forEach(command1 -> Arrays.stream(command1.getAcceptedNames()).forEach(string -> {
            if (string.equalsIgnoreCase(name)) {
                command.set(command1);
            }
        }));

        return command.get();
    }

    private void postSortHacks() {
        this.COMMANDS.sort(Comparator.comparing(Command::getName));
    }

    @Override
    public void save(Logger LOGGER) {
        LOGGER.info("Saved: " + this.getName());
    }

    @SuppressWarnings("unused")
    @EventTarget(value = Priority.HIGHEST)
    public void onChatSentEvent(ChatSentEvent event) {
        Commands COMMANDS = this.getHackManager().getHack(Commands.class);

        if (!COMMANDS.isEnabled()) {
            return;
        }

        boolean runCommand = this.runCommand(COMMANDS.prefix.getValue(), event.ARGS);

        if (runCommand) {
            event.cancel();
        }
    }

    private boolean runCommand(String PREFIX, String ARGS) {

        if (ARGS == null) {
            return false;
        }

        if (ARGS.startsWith(PREFIX)) {

            if (ARGS.equalsIgnoreCase(PREFIX)) {
                this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to dispatch command (no command name inputted)!");
                return true;
            }

            String[] ARGS_X = ARGS.substring(1).split(" ");

            ArrayList<String> PARSED_ARGS_1 = new ArrayList<>();

            for (int i = 0; i < ARGS_X.length; i++) {

                String value = ARGS_X[i];

                value = value.replace(" ", "");

                if (!value.equalsIgnoreCase("")) {
                    PARSED_ARGS_1.add(value);
                }
            }

            String[] ARGS_1 = PARSED_ARGS_1.toArray(new String[0]);

            AtomicBoolean HAS_DISPATCHED = new AtomicBoolean(false);

            this.getCommands().forEach(command -> {

                Arrays.stream(command.getAcceptedNames()).forEach(acceptedName -> {

                    if (acceptedName.equalsIgnoreCase(ARGS_1[0])) {


                        String[] COMMAND_ARGS = Arrays.copyOfRange(ARGS_1, 1, ARGS_1.length);

                        command.onCommand(COMMAND_ARGS);

                        HAS_DISPATCHED.set(true);

                    }
                });
            });

            if (!HAS_DISPATCHED.get()) {
                this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to dispatch command (no command found for '" + this.getColor(ChatColor.AQUA) + ARGS_1[0] + this.getColor(ChatColor.RED) + "')!");
            }

            return true;
        }

        return false;
    }
}
