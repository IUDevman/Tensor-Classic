package dev.tensor.feature.hacks;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.EncryptedStringSetting;

/**
 * @author Hoosiers
 * @since 09-23-2026
 */

@Hack.Info(name = "Translate", category = Category.Misc, description = "Translate chat messages with DeepL.")
public final class Translate extends Hack {

    public final EncryptedStringSetting deepL_API_Code = new EncryptedStringSetting("DeepL API Code", "Input your DeepL API Code here!", "Set the API code for Translate to use.");


}
