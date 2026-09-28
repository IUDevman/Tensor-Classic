package dev.tensor.feature.managers;

import dev.tensor.backend.events.variable.ChatSentEvent;
import dev.tensor.feature.commands.*;
import dev.tensor.feature.hacks.Commands;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.EventPriority;
import dev.tensor.misc.helpers.TensorLogger;
import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.imp.manager.Manager;
import dev.tensor.misc.imp.command.Command;

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
    public void onLoad(TensorLogger LOGGER) {
        this.addCommand(new Accounts());
        this.addCommand(new Capes());
        this.addCommand(new ClearChat());
        this.addCommand(new dev.tensor.feature.commands.Commands());
        this.addCommand(new Config());
        this.addCommand(new FloodLogger());
        this.addCommand(new Friends());
        this.addCommand(new Hacks());
        this.addCommand(new Help());
        this.addCommand(new Options());
        this.addCommand(new Ping());
        this.addCommand(new Prefix());
        this.addCommand(new Reset());
        this.addCommand(new Setting());
        this.addCommand(new Spammer());
        this.addCommand(new TellRaw());
        this.addCommand(new Toggle());
        this.addCommand(new Waypoints());

        this.postSortCommands();
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

    public Command getListCommand(String name) {
        AtomicReference<Command> command = new AtomicReference<>(null);

        this.getCommands().forEach(command1 -> Arrays.stream(command1.getAcceptedNames()).forEach(string -> {
            if (string.equalsIgnoreCase(name)) {
                command.set(command1);
            }
        }));

        return command.get();
    }

    public void postSortCommands() {
        this.COMMANDS.sort(Comparator.comparing(Command::getName));
    }

    @SuppressWarnings("unused")
    private final EventAction<ChatSentEvent> chatSentEventAction = this.addEventAction(this, new EventAction<>(ChatSentEvent.class, EventPriority.Highest, event -> {
        boolean runCommand = this.processCommand(event.ARGS);

        if (runCommand) {
            event.cancel();
        }
    }));

    public boolean processCommand(String message) {
        Commands COMMANDS = this.getHackManager().getHack(Commands.class);

        if (!COMMANDS.isEnabled()) {
            return false;
        }

        return this.runCommand(COMMANDS.prefix.getValue(), message);
    }

    private boolean runCommand(String PREFIX, String ARGS) {
        if (ARGS == null) {
            return false;
        }

        if (ARGS.startsWith(PREFIX)) {

            if (ARGS.equalsIgnoreCase(PREFIX)) {
                this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to dispatch command (no command name inputted)!");
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

                        if (command.shouldParseFullMessage()) {
                            String pre = ARGS_1[0] + " ";
                            String message = ARGS;

                            if (message.length() > pre.length() + 1) {
                                message = message.substring(pre.length() + 1);

                                if (!message.isEmpty()) {
                                    command.onCommand(message);
                                } else {
                                    this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to dispatch command '" + ChatColor.AQUA.text() + command.getName() + ChatColor.RED.text() + "' (no message inputted)!");
                                }
                            } else {
                                this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to dispatch command '" + ChatColor.AQUA.text() + command.getName() + ChatColor.RED.text() + "' (no message inputted)!");
                            }

                            HAS_DISPATCHED.set(true);

                        } else {
                            String[] COMMAND_ARGS = Arrays.copyOfRange(ARGS_1, 1, ARGS_1.length);

                            command.onCommand(COMMAND_ARGS);

                            HAS_DISPATCHED.set(true);
                        }
                    }
                });
            });

            if (!HAS_DISPATCHED.get()) {
                this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to dispatch command (no command found for '" + ChatColor.AQUA.text() + ARGS_1[0] + ChatColor.RED.text() + "')!");
            }

            return true;
        }

        return false;
    }
}
