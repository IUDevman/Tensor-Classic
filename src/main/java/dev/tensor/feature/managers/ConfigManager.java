package dev.tensor.feature.managers;

import com.google.gson.*;
import dev.tensor.TensorClassic;
import dev.tensor.misc.imp.Manager;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

public final class ConfigManager implements Manager {

    @Override
    public String getName() {
        return "ConfigManager";
    }

    private final String configPath = TensorClassic.MOD_NAME + "/";

    @Override
    public void load(Logger LOGGER) {
        LOGGER.info("Loaded: " + this.getName());

        this.setUpClientPath(LOGGER);
    }

    private void setUpClientPath(Logger LOGGER) {
        try {
            if (!Files.exists(Paths.get(this.getConfigPath()))) {
                Files.createDirectories(Paths.get(this.getConfigPath()));
            }

        } catch (IOException ignored) {
            LOGGER.info("Failed to load: " + this.getName());
        }
    }

    @Override
    public void save(Logger LOGGER) {
        LOGGER.info("Saved: " + this.getName());
    }

    public String getConfigPath() {
        return this.configPath;
    }

    public void postLoad(Manager manager) {
        this.getTensorLogger().info("Loaded Config for: " + manager.getName() + "!");
    }

    public void postSave(Manager manager) {
        this.getTensorLogger().info("Saved Config for: " + manager.getName() + "!");
    }

    public JsonObject getJsonObjectFromPath(Path path) throws Exception {
        InputStream inputStream = Files.newInputStream(path);
        return new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();
    }

    public JsonArray getJsonArrayFromPath(Path path) throws Exception {
        InputStream inputStream = Files.newInputStream(path);
        return new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonArray();
    }

    public void registerFile(Path path) throws IOException {
        if (Files.exists(path)) Files.delete(path);

        Files.createFile(path);
    }

    public void createAndWrite(Path path, JsonObject jsonObject) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(path.toString()), StandardCharsets.UTF_8);

        String jsonString = gson.toJson(new JsonParser().parse(jsonObject.toString()));
        outputStreamWriter.write(jsonString);
        outputStreamWriter.close();
    }

    public void createAndWrite(Path path, JsonArray jsonArray) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(path.toString()), StandardCharsets.UTF_8);

        String jsonString = gson.toJson(new JsonParser().parse(jsonArray.toString()));
        outputStreamWriter.write(jsonString);
        outputStreamWriter.close();
    }
}
