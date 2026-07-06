package dev.tensor.misc.helpers;

import dev.tensor.feature.hacks.ClickGUI;
import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.imp.Globals;
import dev.tensor.misc.imp.setting.settings.SpammerListSetting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Hoosiers
 * @since 07-03-2026
 */

public final class SpammerList implements Globals {

    private final ArrayList<String> pathsToSpammerFiles = new ArrayList<>();

    private final ArrayList<String> spammerFileNames = new ArrayList<>();

    private final ArrayList<String> currentSpammerLines = new ArrayList<>();

    private String currentSpammerFileName = null;

    private SpammerListSetting setting = null;

    @SuppressWarnings("resource")
    public SpammerList(String clientPath) {
        this.findAndLoadFiles(clientPath, true);
    }

    public void resetSpammer(String clientPath) {
        this.findAndLoadFiles(clientPath, false);

        ClickGUI clickGUI = this.getHackManager().getHack(ClickGUI.class);

        if (clickGUI != null) {
            TensorGUI tensorGUI = clickGUI.tensorGUI;

            if (tensorGUI != null) {
                tensorGUI.resetSpammerImp();
            }
        }
    }

    private void findAndLoadFiles(String clientPath, boolean clearCurrent) {
        this.spammerFileNames.clear();
        this.pathsToSpammerFiles.clear();

        Path path = Paths.get(clientPath + "Spammer/");

        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            Files.list(path).filter(Files::isRegularFile).forEach(file -> {
                if (file.toString().endsWith(".txt")) {
                    this.pathsToSpammerFiles.add(file.toString());
                }
            });
        } catch (Exception ignored) {
            this.getTensorLogger().info("Failed to load spammer files!");
        }

        if (this.pathsToSpammerFiles.isEmpty()) {
            return;
        }

        this.pathsToSpammerFiles.forEach(string -> {

            String[] split = string.split("\\\\");

            String name = split[split.length - 1];

            if (name == null || name.length() < 5) {
                return;
            }

            this.spammerFileNames.add(name.substring(0, name.length() - 4));
        });

        if (!clearCurrent && !this.spammerFileNames.contains(this.currentSpammerFileName)) {
            clearCurrent = true;
        }

        if (clearCurrent) {
            this.currentSpammerFileName = null;
            this.currentSpammerLines.clear();

            try {
                this.loadSpammerFile(this.pathsToSpammerFiles.get(0));

            } catch (Exception ignored) {
                this.getTensorLogger().info("Failed to load lines for spammer file '" + this.pathsToSpammerFiles.get(0) + "'!");
            }
        }
    }

    private void loadSpammerFile(String path) throws IOException {
        Scanner scanner = new Scanner(Paths.get(path));

        this.currentSpammerLines.clear();

        while (scanner.hasNextLine()) {
            this.currentSpammerLines.add(this.filterBlockedChars(scanner.nextLine()));
        }

        String[] split = path.split("\\\\");

        String end = split[split.length - 1];

        this.currentSpammerFileName = end.substring(0, end.length() - 4);

        if (this.setting != null) {
            this.setting.onChange();
        }
    }

    public ArrayList<String> getFileNames() {
        return this.spammerFileNames;
    }

    public String getCurrentSpammerFileName() {
        return this.currentSpammerFileName;
    }

    public boolean isCurrentSpammer(String name) {
        return this.currentSpammerFileName.equalsIgnoreCase(name);
    }

    public boolean isSpammerLoaded(String name) {
        return this.spammerFileNames.stream().filter(string -> string.equalsIgnoreCase(name)).findFirst().orElse(null) != null;
    }

    @SuppressWarnings("UnusedReturnValue")
    public int setCurrentSpammer(String newSpammer, boolean nullByPass) {

        if (newSpammer == null || newSpammer.isEmpty() || newSpammer.equalsIgnoreCase(" ")) {
            if (nullByPass) {
                this.currentSpammerFileName = null;
                this.currentSpammerLines.clear();
            }

            return -1; //No name imputted
        }

        if (newSpammer.toLowerCase().endsWith(".txt")) {
            if (newSpammer.length() < 5) {
                if (nullByPass) {
                    this.currentSpammerFileName = null;
                    this.currentSpammerLines.clear();
                }

                return -1; //No name imputted
            }

            newSpammer = newSpammer.substring(0, newSpammer.length() - 4);
        }

        if (this.currentSpammerFileName.equalsIgnoreCase(newSpammer)) {
            return 0; //Currently loaded
        }

        if (this.spammerFileNames.isEmpty()) {
            return 1; //Not found - empty folder
        }

        final String finalNewSpammer = newSpammer;

        boolean hasFile = this.spammerFileNames.stream().filter(string -> string.equalsIgnoreCase(finalNewSpammer)).findFirst().orElse(null) != null;

        if (!hasFile) {
            return 2; //Not found - no name for this
        }

        final AtomicReference<String> newPath = new AtomicReference<>(null);

        this.pathsToSpammerFiles.forEach(string -> {

            if (newPath.get() != null) {
                return;
            }

            String[] split = string.split("\\\\");

            String end = split[split.length - 1];

            if (end.substring(0, end.length() - 4).equalsIgnoreCase(finalNewSpammer)) {
                newPath.set(string);
            }
        });

        if (newPath.get() == null) {
            return 3; //Path not found
        }

        try {
            this.loadSpammerFile(newPath.get());

        } catch (Exception ignored) {
            return 4; //Failed exception
        }

        return 5; //Successful return
    }

    @SuppressWarnings("unused")
    public void removeSpammer(String spammerName) {
        ArrayList<String> toRemovePath = new ArrayList<>();
        ArrayList<String> toRemoveName = new ArrayList<>();

        this.pathsToSpammerFiles.forEach(string -> {

            String[] split = string.split("\\\\");

            String end = split[split.length - 1];

            if (end.substring(0, end.length() - 4).equalsIgnoreCase(spammerName)) {
                toRemovePath.add(string);
            }
        });

        this.pathsToSpammerFiles.removeAll(toRemovePath);

        this.spammerFileNames.forEach(string -> {

            if (string.equalsIgnoreCase(spammerName)) {
                toRemoveName.add(string);
            }
        });

        this.spammerFileNames.removeAll(toRemoveName);

        if (this.currentSpammerFileName.equalsIgnoreCase(spammerName)) {
            this.currentSpammerFileName = null;
            this.currentSpammerLines.clear();
            this.setting.onChange();
        }
    }

    public ArrayList<String> getSpammerLines() {
        return this.currentSpammerLines;
    }

    public void setSetting(SpammerListSetting setting) {
        this.setting = setting;
    }

    private String filterBlockedChars(String input) {
        final String allowedChars = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

        StringBuilder fixed = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == '§' || allowedChars.indexOf(c) == -1) {
                fixed.append('a');
            } else {
                fixed.append(c);
            }
        }

        return fixed.toString();
    }
}
