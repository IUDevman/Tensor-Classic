package dev.tensor.misc.gui;

import dev.tensor.misc.imp.command.Command;
import dev.tensor.misc.imp.command.SimpleCallableCommand;
import dev.tensor.misc.imp.render.Render2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Hoosiers
 * @since 09-03-2021
 */

public final class TensorChatGUI implements Render2D {

    public void render(String prefix, String message, int minX, int minY, int maxX, int maxY, int fillColor, int outlineColor, int textColor) {

        this.drawRectangle(minX, minY - 1, maxX, minY, outlineColor);
        this.drawRectangle(minX, maxY, maxX, maxY + 1, outlineColor);
        this.drawRectangle(minX - 1, minY - 1, minX, maxY + 1, outlineColor);
        this.drawRectangle(maxX, minY - 1, maxX + 1, maxY + 1, outlineColor);

        int startX = minX + this.getFontRenderer().getStringWidth(prefix);
        int startY = minY - 13;

        if (message.equalsIgnoreCase(prefix)) {
            this.renderTip(prefix + "Commands", startX, startY, minY, fillColor, outlineColor, textColor);
            return;
        }

        String parsedMessage = message.replaceFirst(prefix, "");

        String[] wordsX = parsedMessage.split(" ");

        ArrayList<String> parsedWords = new ArrayList<>();

        for (int i = 0; i < wordsX.length; i++) {

            String value = wordsX[i];

            value = value.replace(" ", "");

            if (!value.equalsIgnoreCase("")) {
                parsedWords.add(value);
            }
        }

        String[] words = parsedWords.toArray(new String[0]);

        if (words.length < 1) {
            this.renderTip(prefix + "Commands", startX, startY, minY, fillColor, outlineColor, textColor);
            return;
        }

        Command command = this.getCommandManager().getListCommand(words[0]);

        if (command == null && words.length == 1) {

            ArrayList<Command> commands = this.getCommandManager().getCommandsThatStartWith(words[0]);

            if (commands.isEmpty()) {
                this.renderTip("No Commands Found!", startX, startY, minY, fillColor, outlineColor, textColor);
                return;
            }

            ArrayList<String> commandNames = new ArrayList<>();

            commands.forEach(command1 -> Arrays.stream(command1.getAcceptedNames()).forEach(string -> {
                if (string.toLowerCase().startsWith(words[0].toLowerCase())) {
                    commandNames.add(string);
                }
            }));

            this.renderTip(prefix + commandNames, startX, startY, minY, fillColor, outlineColor, textColor);
            return;
        }

        if (command == null) {
            this.renderTip("No Commands Found!", startX, startY, minY, fillColor, outlineColor, textColor);
            return;
        }

        if (command instanceof SimpleCallableCommand) {
            this.renderTip(prefix + command.getName() + ": " + command.getDescription(), startX, startY, minY, fillColor, outlineColor, textColor);
            return;
        }

        String[] regexArray = command.getRegexInArray();

        regexArray[0] = command.getName();

        boolean clear = false;

        for (int i = 1; i < regexArray.length; i++) {

            if (clear) {
                regexArray[i] = "";
            }

            if (words.length >= i + 1) {

                String word = words[i];

                if (word.equalsIgnoreCase("list")) {
                    clear = true;
                }

                regexArray[i] = word;
            } else if (regexArray[i].contains("$")) {
                regexArray[i] = regexArray[i].replace("$", " ");
            }
        }

        AtomicReference<String> regexString = new AtomicReference<>("");

        Arrays.stream(regexArray).forEach(string -> regexString.set(regexString.get() + " " + string));

        this.renderTip(prefix + regexString.get().substring(1), startX, startY, minY, fillColor, outlineColor, textColor);
    }

    private void renderTip(String tip, int minX, int minY, int maxY, int fillColor, int outlineColor, int textColor) {

        //LAZY
        minY -= 1;
        maxY -= 1;

        this.drawRectangle(minX - 2, minY - 2, minX + this.getFontRenderer().getStringWidth(tip) + 2, maxY - 1, fillColor);
        this.drawRectangle(minX - 3, minY - 3, minX + this.getFontRenderer().getStringWidth(tip) + 3, minY - 2, outlineColor);
        this.drawRectangle(minX - 3, minY + 11, minX + this.getFontRenderer().getStringWidth(tip) + 3, minY + 12, outlineColor);
        this.drawRectangle(minX - 3, minY - 2, minX - 2, maxY - 1, outlineColor);
        this.drawRectangle(minX + this.getFontRenderer().getStringWidth(tip) + 2, minY - 2, minX + this.getFontRenderer().getStringWidth(tip) + 3, maxY - 1, outlineColor);


        this.drawString(tip, minX, minY, textColor);
    }
}
