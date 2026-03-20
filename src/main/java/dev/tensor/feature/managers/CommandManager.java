package dev.tensor.feature.managers;

import dev.tensor.backend.events.ChatSentEvent;
import dev.tensor.feature.commands.HackList;
import dev.tensor.feature.commands.Prefix;
import dev.tensor.feature.commands.Reset;
import dev.tensor.feature.hacks.Commands;
import dev.tensor.misc.command.Command;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.Manager;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;

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

        this.addCommand(new HackList());
        this.addCommand(new Prefix());
        this.addCommand(new Reset());

        this.postSortHacks();
    }

    public void addCommand(Command COMMAND) {
        this.COMMANDS.add(COMMAND);
    }

    public ArrayList<Command> getCommands() {
        return this.COMMANDS;
    }

    private void postSortHacks() {
        this.COMMANDS.sort(Comparator.comparing(Command::getName));
    }

    @Override
    public void save(Logger LOGGER) {
        LOGGER.info("Saved: " + this.getName());
    }

    @SuppressWarnings("unused")
    @EventTarget
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

            String[] ARGS_1 = ARGS.substring(1).split(" ");

            AtomicBoolean HAS_DISPATCHED = new AtomicBoolean(false);

            this.getCommands().forEach(command -> {

                Arrays.stream(command.getAcceptedNames()).forEach(acceptedName -> {

                    if (acceptedName.equalsIgnoreCase(ARGS_1[0])) {


                        String[] COMMAND_ARGS = Arrays.copyOfRange(ARGS_1,  1, ARGS_1.length);

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
