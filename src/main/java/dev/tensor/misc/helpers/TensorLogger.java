package dev.tensor.misc.helpers;

import dev.tensor.TensorClassic;
import dev.tensor.feature.managers.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 09-15-2026
 */

public final class TensorLogger {

    private static TensorLogger INSTANCE;

    public static TensorLogger getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TensorLogger();
        }

        return INSTANCE;
    }

    private static final boolean USE_SYS_OUT = false;
    private static final boolean SAVE_LOGS = true;

    private final Logger LOGGER;

    private final ArrayList<String> LOGGED_MESSAGES = new ArrayList<>();

    private TensorLogger() {
        this.LOGGER = LoggerFactory.getLogger(TensorClassic.MOD_NAME);
    }

    public void log(String message, Level level) {
        if (USE_SYS_OUT) {
            System.out.println("[" + TensorClassic.MOD_NAME + "] " + message + " (" + level.name() + ")");
            return;
        }

        level.process(LOGGER, message);

        if (SAVE_LOGS) {
            this.addLogToHistory(message, level);
        }
    }

    private void addLogToHistory(String message, Level level) {
        if (this.LOGGED_MESSAGES.size() > 1000) {
            return;
        }

        message = "(" + level.name() + "): " + message;

        this.LOGGED_MESSAGES.add(message);
    }

    public void generateLogReport(ConfigManager cm) {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTime = time.format(formatter).replace(" ", "-").replace(":", "-");

        String folder = cm.getConfigPath() + "/Logs/";
        Path folderPath = Paths.get(folder);

        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectories(folderPath);

            } catch (IOException ignored) {
                this.log("Failed to generate log report (could not make folder)!", Level.ERROR);
            }
        }

        String fileName = TensorClassic.MOD_NAME_SHORT + "_LOGS_" + currentTime;
        String file = folder + fileName + ".txt";
        Path filePath = Paths.get(file);

        try {
            Files.createFile(filePath);

        } catch (IOException ignored) {
            this.log("Failed to generate log report (could not make txt file)!", Level.ERROR);
        }

        try {
            Files.write(filePath, this.LOGGED_MESSAGES);

        } catch (IOException ignored) {
            this.log("Failed to generate log report (could not write txt file)!", Level.ERROR);
        }
    }

    public enum Level {
        INFO,
        WARN,
        ERROR;

        public void process(Logger LOGGER, String message) {
            switch (this) {
                case WARN:
                    LOGGER.warn(message);
                    break;
                case ERROR:
                    LOGGER.error(message);
                    break;
                default:
                    LOGGER.info(message);
                    break;
            }
        }
    }
}
