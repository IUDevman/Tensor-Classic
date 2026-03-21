package dev.tensor.backend.mixins;

import dev.tensor.backend.events.WeatherEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

@Mixin(value = WorldInfo.class, priority = Integer.MAX_VALUE)
public final class WorldInfoMixin implements Globals {

    @Inject(method = "getRaining", at = @At("HEAD"), cancellable = true)
    public void onGetRaining(CallbackInfoReturnable<Boolean> cir) {

        if ($tensor_HandleWeatherEvent()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getThundering", at = @At("HEAD"), cancellable = true)
    public void onGetThundering(CallbackInfoReturnable<Boolean> cir) {

        if ($tensor_HandleWeatherEvent()) {
            cir.setReturnValue(false);
        }
    }

    @Unique
    private boolean $tensor_HandleWeatherEvent() {
        if (this.failsNullCheck()) {
            return false;
        }

        WeatherEvent event = new WeatherEvent();
        this.getEventManager().call(event);

        return event.isCancelled();
    }
}
