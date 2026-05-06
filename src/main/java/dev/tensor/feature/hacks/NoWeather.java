package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.WeatherEvent;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

@Hack.Info(name = "NoWeather", category = Category.Render)
public final class NoWeather extends Hack {

    @SuppressWarnings("unused")
    public void onWeatherEvent(WeatherEvent event) {
        event.cancel();
    }
}
