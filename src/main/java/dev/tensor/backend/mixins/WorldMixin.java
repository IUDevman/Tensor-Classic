package dev.tensor.backend.mixins;

import dev.tensor.backend.events.LightValueEvent;
import dev.tensor.backend.events.VelocityEvent;
import dev.tensor.backend.events.WeatherEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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

    @Inject(method = "func_27163_E", at = @At("HEAD"), cancellable = true)
    public void onSetWeatherStrength(CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        WeatherEvent event = new WeatherEvent();
        this.getEventManager().call(event);

        if (event.isCancelled()) {
            ci.cancel();
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

    @Redirect(method = "handleMaterialAcceleration", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Vec3D;lengthVector()D"))
    public double test(Vec3D instance) {
        if (this.failsNullCheck()) {
            return instance.lengthVector();
        }

        VelocityEvent event = new VelocityEvent(VelocityEvent.Type.Fluid);
        this.getEventManager().call(event);

        if (event.isCancelled()) {
            return 0;
        }

        return instance.lengthVector();
    }

}
