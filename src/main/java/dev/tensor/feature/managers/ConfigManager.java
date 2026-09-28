package dev.tensor.feature.managers;

import com.google.gson.*;
import dev.tensor.TensorClassic;
import dev.tensor.misc.helpers.TensorLogger;
import dev.tensor.misc.imp.manager.Manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
    public void onLoad(TensorLogger LOGGER) {
        this.setUpClientPath(LOGGER);
    }

    private void setUpClientPath(TensorLogger LOGGER) {
        try {
            if (!Files.exists(Paths.get(this.getConfigPath()))) {
                Files.createDirectories(Paths.get(this.getConfigPath()));
            }

        } catch (IOException ignored) {
            LOGGER.log("Failed to load config for: " + this.getName(), TensorLogger.Level.ERROR);
        }
    }

    public String getConfigPath() {
        return this.configPath;
    }

    public JsonObject getJsonObjectFromPath(Path path) throws Exception {
        return this.getJsonElementFromPath(path).getAsJsonObject();
    }

    public JsonArray getJsonArrayFromPath(Path path) throws Exception {
        return this.getJsonElementFromPath(path).getAsJsonArray();
    }

    private JsonElement getJsonElementFromPath(Path path) throws Exception {
        InputStream inputStream = Files.newInputStream(path);

        return JsonParser.parseReader(new InputStreamReader(inputStream));
    }

    public void registerFile(Path path) throws IOException {
        if (Files.exists(path)) Files.delete(path);

        Files.createFile(path);
    }

    public void createAndWrite(Path path, JsonObject jsonObject) throws Exception {
        this.createAndWrite(path, jsonObject.toString());
    }

    public void createAndWrite(Path path, JsonArray jsonArray) throws Exception {
        this.createAndWrite(path, jsonArray.toString());
    }

    private void createAndWrite(Path path, String objectString) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(Files.newOutputStream(Paths.get(path.toString())), StandardCharsets.UTF_8);

        String jsonString = gson.toJson(JsonParser.parseString(objectString));
        outputStreamWriter.write(jsonString);
        outputStreamWriter.close();
    }
}
