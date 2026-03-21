package dev.tensor.feature.hacks;

import dev.tensor.backend.events.WeatherEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import org.lwjgl.input.Keyboard;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

@Hack.Info(name = "NoWeather", category = Category.Render, bind = Keyboard.KEY_H)
public final class NoWeather extends Hack {

    @SuppressWarnings("unused")
    @EventTarget
    public void onWeatherEvent(WeatherEvent event) {
        event.cancel();
    }
}
