package dev.tensor.backend.mixins;

import dev.tensor.backend.events.LightValueEvent;
import dev.tensor.backend.events.WeatherEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

@Mixin(value = World.class, priority = Integer.MAX_VALUE)
public abstract class WorldMixin implements Globals {

    @Inject(method = "getBrightness", at = @At("HEAD"), cancellable = true)
    public void onGetBrightness(int i, int j, int k, int l, CallbackInfoReturnable<Float> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        LightValueEvent event = new LightValueEvent();
        this.getEventManager().call(event);

        if (event.isCancelled()) {
            cir.setReturnValue(1F);
        }
    }

    @Inject(method = "func_27166_f", at = @At("HEAD"), cancellable = true)
    public void thunderStrength(float par1, CallbackInfoReturnable<Float> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        WeatherEvent event = new WeatherEvent();
        this.getEventManager().call(event);

        if (event.isCancelled()) {
            cir.setReturnValue(0.0F);
        }
    }

    @Inject(method = "func_27162_g", at = @At("HEAD"), cancellable = true)
    public void rainStrength(float par1, CallbackInfoReturnable<Float> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        WeatherEvent event = new WeatherEvent();
        this.getEventManager().call(event);

        if (event.isCancelled()) {
            cir.setReturnValue(0.0F);
        }
    }
}
