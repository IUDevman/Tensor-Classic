package dev.tensor.feature.managers;

import com.deepl.api.DeepLClient;
import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import dev.tensor.feature.hacks.Translate;
import dev.tensor.misc.helpers.TensorLogger;
import dev.tensor.misc.imp.Manager;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hoosiers
 * @since 09-22-2026
 */

public final class DeepLManager implements Manager {

    private final Translate translate;

    private final AtomicInteger billedCharacters = new AtomicInteger(0);

    /**
     * This is to try to cheapen the translation costs since a free account only allows for a small amount of translations.
     *
     * HashMap 1: Key - Language code of translated object; Object - HashMap2
     *
     * HashMap 2: Key - String before translation; Object - String after translation.
     */
    private final HashMap<String, HashMap<String, String>> BIG_LANGUAGE_MAP = new HashMap<>();

    private DeepLClient client;

    private String API_CODE;

    @Override
    public String getName() {
        return "DeepLManager";
    }

    public DeepLManager() {
        this.translate = this.getHackManager().getHack(Translate.class);

        this.setAPICode(this.API_CODE);
        this.loadSavedLanguageMap();
    }

    /*
        import java.io.*;
        import java.util.HashMap;

        public class MapSerializer {

        // Save the nested HashMap to a file
        public static void saveMap(HashMap<String, HashMap<String, String>> map, String filePath) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
                oos.writeObject(map);
                System.out.println("Map saved successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Load the nested HashMap from a file
        @SuppressWarnings("unchecked")
        public static HashMap<String, HashMap<String, String>> loadMap(String filePath) {
            HashMap<String, HashMap<String, String>> map = null;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
                map = (HashMap<String, HashMap<String, String>>) ois.readObject();
                System.out.println("Map loaded successfully.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return map;
        }
    }
     */

    private void loadSavedLanguageMap() {
        //todo; save already translated messages to language map
    }

    public void setAPICode(String NEW_API_CODE) {
        String OLD_API_CODE = this.API_CODE;

        this.API_CODE = NEW_API_CODE;

        if (!this.API_CODE.equalsIgnoreCase(OLD_API_CODE) && !this.API_CODE.isEmpty() && !this.API_CODE.equalsIgnoreCase(this.translate.deepL_API_Code.getDefaultValue())) {
            try {
                this.client = new DeepLClient(this.API_CODE);

            } catch (IllegalArgumentException ignored) {
                this.getTensorLogger().log("Invalid DeepL API Code Inputted!", TensorLogger.Level.WARN);
                this.client = null;
            }
        }
    }

    public String translate(String toTranslate, Language languageToTranslateTo) {
        HashMap<String, String> MAP_OF_TRANSLATIONS_FOR_LANGUAGE = this.BIG_LANGUAGE_MAP.get(languageToTranslateTo.getCode());

        if (MAP_OF_TRANSLATIONS_FOR_LANGUAGE != null && !MAP_OF_TRANSLATIONS_FOR_LANGUAGE.isEmpty()) {
            String translatedString = MAP_OF_TRANSLATIONS_FOR_LANGUAGE.get(toTranslate);

            if (translatedString != null) {
                return translatedString;
            }
        }

        if (this.API_CODE.equalsIgnoreCase("none") || this.client == null) {
            //We really don't wamt to spam the console with info or warning logs over this.
            return "®Failed to translate text! Bad API key or null DeepL client!";
        }

        try {
            TextResult result = this.client.translateText(toTranslate, null, languageToTranslateTo.getCode());
            this.billedCharacters.addAndGet(result.getBilledCharacters());

            if (this.BIG_LANGUAGE_MAP.get(languageToTranslateTo.getCode()) == null) {
                HashMap<String, String> RESULTS_FOR_LANGUAGE = new HashMap<>();
                RESULTS_FOR_LANGUAGE.put(toTranslate, result.getText());

                this.BIG_LANGUAGE_MAP.put(languageToTranslateTo.getCode(), RESULTS_FOR_LANGUAGE);

            } else {
                HashMap<String, String> RESULTS_FOR_LANGUAGE = this.BIG_LANGUAGE_MAP.get(languageToTranslateTo.getCode());

                if (!RESULTS_FOR_LANGUAGE.containsKey(toTranslate)) {
                    RESULTS_FOR_LANGUAGE.put(toTranslate, result.getText());
                }
            }

            return result.getText();

        } catch (DeepLException ignored) {
            this.getTensorLogger().log("Failed to translate text due to exception with DeepL API!", TensorLogger.Level.WARN);
            return "®Failed to translate text! Error with DeepL API.";

        } catch (InterruptedException ignored) {
            this.getTensorLogger().log("Failed to translate text due to unknown exception!", TensorLogger.Level.WARN);
            return "®Failed to translate text! Unknown exception.";
        }
    }

    public int getBilledCharacters() {
        return this.billedCharacters.get();
    }

    public enum Language {
        Arabic("ar"),
        Bulgarian("bg"),
        Chinese("zh"),
        Czech("cs"),
        Danish("da"),
        Dutch("n1"),
        English("en"),
        Estonian("et"),
        Finnish("fi"),
        French("fr"),
        German("de"),
        Greek("el"),
        Indonesian("id"),
        Italian("it"),
        Japanese("ja"),
        Korean("ko"),
        Polish("pl"),
        Portuguese("pt"),
        Russian("ru"),
        Spanish("es"),
        Turkish("tr"),
        Ukrainian("uk"),
        Vietnamese("vi");

        private final String code;

        Language(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }
    }
}
