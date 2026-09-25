package dev.tensor.feature.managers;

import com.deepl.api.DeepLClient;
import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import dev.tensor.feature.hacks.Translate;
import dev.tensor.misc.helpers.TensorLogger;
import dev.tensor.misc.imp.Manager;
import dev.tensor.misc.imp.ManagerPostOrder;
import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.Packet3Chat;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hoosiers
 * @since 09-22-2026
 */

public final class DeepLManager implements Manager {

    /**
     * This is to try to cheapen the translation costs since a free account only allows for a small amount of translations.
     * HashMap 1: Key - Language code of translated object; Object - HashMap2
     * HashMap 2: Key - String before translation; Object - String after translation.
     */
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, String>> BIG_LANGUAGE_MAP = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> MESSAGE_QUEUE = new ConcurrentHashMap<>();
    private final AtomicInteger billedCharacters = new AtomicInteger(0);

    private Translate translate;
    private DeepLClient client;
    private String API_CODE;

    @Override
    public String getName() {
        return "DeepLManager";
    }

    @Override
    public void onSave(TensorLogger LOGGER) {
        this.saveCachedLanguageMap(LOGGER);
    }

    @Override
    public void onLoadPost(TensorLogger LOGGER) {
        this.translate = this.getHackManager().getHack(Translate.class);

        this.setAPICode(this.translate.deepL_API_Code.getValue());
        this.loadCachedLanguageMap(LOGGER);

        Thread sendQueueThread = new Thread(() -> {
            while (true) {
                if (!this.MESSAGE_QUEUE.isEmpty()) {

                    if (!this.failsNullCheck() && this.isMultiplayer()) {
                        Map.Entry<String, String> firstEntry = this.MESSAGE_QUEUE.entrySet().iterator().next();

                        if (firstEntry != null) {
                            String translated = this.getDeepLManager().translate(firstEntry.getKey(), firstEntry.getValue());

                            if (translated != null && !translated.isEmpty() && !translated.contains("®")) {
                                String scrubbed = translated;

                                for (int i = 0; i < translated.length(); i++) {
                                    String c = String.valueOf(translated.charAt(i));

                                    if (!ChatAllowedCharacters.allowedCharacters.contains(c)) {
                                        scrubbed = scrubbed.replace(c, "X");
                                        //todo: collect the list of common swaps, add other replacements.
                                        this.getTensorLogger().log(c, TensorLogger.Level.INFO);
                                    }
                                }

                                if (scrubbed.length() > 100) {
                                    scrubbed = scrubbed.substring(0, 99);
                                }

                                this.sendPacket(new Packet3Chat("®" + scrubbed));
                            }

                            this.MESSAGE_QUEUE.remove(firstEntry.getKey());
                        }
                    }
                }
            }
        });

        sendQueueThread.start();
    }

    @Override
    public int getPostOrder() {
        return ManagerPostOrder.DEEPL_MANAGER.getValue();
    }

    @SuppressWarnings("unchecked")
    private void loadCachedLanguageMap(TensorLogger LOGGER) {
        Path path = Paths.get(this.getConfigManager().getConfigPath() + "DeepLCache.txt");

        if (!Files.exists(path)) return;

        try {
            ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(path));

            ConcurrentHashMap<String, ConcurrentHashMap<String, String>> cached = (ConcurrentHashMap<String, ConcurrentHashMap<String, String>>) stream.readObject();

            if (cached != null && !cached.isEmpty()) {
                this.BIG_LANGUAGE_MAP.putAll(cached);
                LOGGER.log("Loaded cached language map for " + this.getName() + "!", TensorLogger.Level.INFO);
            }

        } catch (Exception ignored) {
            LOGGER.log("Failed to load cached language map for " + this.getName() + "!", TensorLogger.Level.WARN);
        }
    }

    private void saveCachedLanguageMap(TensorLogger LOGGER) {
        Path path = Paths.get(this.getConfigManager().getConfigPath() + "DeepLCache.txt");

        try {
            this.getConfigManager().registerFile(path);

            ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(path));

            stream.writeObject(this.BIG_LANGUAGE_MAP);
            LOGGER.log("Saved cached language map for " + this.getName() + "!", TensorLogger.Level.INFO);

        } catch (Exception ignored) {
            LOGGER.log("Failed to save cached language map for " + this.getName() + "!", TensorLogger.Level.WARN);
        }
    }

    public void setAPICode(String NEW_API_CODE) {
        String OLD_API_CODE = this.API_CODE;

        this.API_CODE = NEW_API_CODE;

        if (this.API_CODE == null) {
            return;
        }

        if (this.API_CODE.equals(OLD_API_CODE)) {
            return;
        }

        if (this.translate == null) {
            return;
        }

        if (!this.API_CODE.isEmpty() && !this.API_CODE.equalsIgnoreCase(this.translate.deepL_API_Code.getDefaultValue())) {
            try {
                this.client = new DeepLClient(this.API_CODE);

            } catch (IllegalArgumentException ignored) {
                this.getTensorLogger().log("Invalid DeepL API Code Inputted!", TensorLogger.Level.WARN);
                this.client = null;
            }
        }
    }

    private String translate(String toTranslate, String languageToTranslateTo) {
        ConcurrentHashMap<String, String> MAP_OF_TRANSLATIONS_FOR_LANGUAGE = this.BIG_LANGUAGE_MAP.get(languageToTranslateTo);

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
            TextResult result = this.client.translateText(toTranslate, null, languageToTranslateTo);
            this.billedCharacters.addAndGet(result.getBilledCharacters());

            if (this.BIG_LANGUAGE_MAP.get(languageToTranslateTo) == null) {
                ConcurrentHashMap<String, String> RESULTS_FOR_LANGUAGE = new ConcurrentHashMap<>();
                RESULTS_FOR_LANGUAGE.put(toTranslate, result.getText());

                this.BIG_LANGUAGE_MAP.put(languageToTranslateTo, RESULTS_FOR_LANGUAGE);

            } else {
                ConcurrentHashMap<String, String> RESULTS_FOR_LANGUAGE = this.BIG_LANGUAGE_MAP.get(languageToTranslateTo);

                if (!RESULTS_FOR_LANGUAGE.containsKey(toTranslate)) {
                    RESULTS_FOR_LANGUAGE.put(toTranslate, result.getText());
                }
            }

            this.getTensorLogger().log("Total DeepL API billable characters (for this session): " + this.billedCharacters.get() + "!", TensorLogger.Level.INFO);
            return result.getText();

        } catch (DeepLException ignored) {
            this.getTensorLogger().log("Failed to translate text due to exception with DeepL API!", TensorLogger.Level.WARN);
            return "®Failed to translate text! Error with DeepL API.";

        } catch (InterruptedException ignored) {
            this.getTensorLogger().log("Failed to translate text due to unknown exception!", TensorLogger.Level.WARN);
            return "®Failed to translate text! Unknown exception.";
        }
    }

    public void addMessageToQueue(String message, Language languageToTranslateTo) {
        this.MESSAGE_QUEUE.put(message, languageToTranslateTo.getCode());
    }

    //Some of these are supported by DeepL but not by minecraft chat... May work for future game versions.
    public enum Language {
        //Arabic("ar"),
        Bulgarian("bg"),
        //Chinese("zh"),
        Czech("cs"),
        Danish("da"),
        Dutch("n1"),
        English("en"),
        Estonian("et"),
        Finnish("fi"),
        French("fr"),
        German("de"),
        //Greek("el"), /* Sorry Cyber */
        //Indonesian("id"),
        Italian("it"),
        //Japanese("ja"),
        //Korean("ko"),
        Polish("pl"),
        Portuguese("pt"),
        //Russian("ru"),
        Spanish("es"),
        Turkish("tr");
        //Ukrainian("uk"),
        //Vietnamese("vi");

        private final String code;

        Language(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }
    }
}
