package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.constant.LightValueEvent;
import dev.tensor.backend.events.constant.WeatherEvent;
import dev.tensor.backend.events.variable.VelocityEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.Entity;
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

@Mixin(value = World.class, priority = MixinPriority.VALUE)
public abstract class WorldMixin implements Globals {

    @Inject(method = "getBrightness", at = @At("HEAD"), cancellable = true)
    public void tensor_onGetBrightness(int i, int j, int k, int l, CallbackInfoReturnable<Float> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        LightValueEvent event = EventCache.getCachedEvent(LightValueEvent.class).call();

        if (event.isCancelled()) {
            cir.setReturnValue(1F);
        }
    }

    @Inject(method = "func_27163_E", at = @At("HEAD"), cancellable = true)
    public void tensor_onSetWeatherStrength(CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        WeatherEvent event = EventCache.getCachedEvent(WeatherEvent.class).call();

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "func_27166_f", at = @At("HEAD"), cancellable = true)
    public void tensor_thunderStrength(float par1, CallbackInfoReturnable<Float> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        WeatherEvent event = EventCache.getCachedEvent(WeatherEvent.class).call();

        if (event.isCancelled()) {
            cir.setReturnValue(0.0F);
        }
    }

    @Inject(method = "func_27162_g", at = @At("HEAD"), cancellable = true)
    public void tensor_rainStrength(float par1, CallbackInfoReturnable<Float> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        WeatherEvent event = EventCache.getCachedEvent(WeatherEvent.class).call();

        if (event.isCancelled()) {
            cir.setReturnValue(0.0F);
        }
    }

    @Redirect(method = "handleMaterialAcceleration", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Vec3D;lengthVector()D"))
    public double tensor_onHandleMaterialAccelerationInvokeVector(Vec3D instance) {
        if (this.failsNullCheck()) {
            return instance.lengthVector();
        }

        VelocityEvent event = EventCache.getCachedEvent(VelocityEvent.class).withVariables(VelocityEvent.Type.Fluid).call();

        if (event.isCancelled()) {
            return 0;
        }

        return instance.lengthVector();
    }

    @Inject(method = "playSoundAtEntity", at = @At("HEAD"), cancellable = true)
    public void tensor_onPlaySoundAtEntity(Entity string, String f, float g, float par4, CallbackInfo ci) {
        if (this.failsNullCheck() || f == null) {
            return;
        }

        if (f.equalsIgnoreCase("mob.wolf.shake")) {
            WeatherEvent event = EventCache.getCachedEvent(WeatherEvent.class).call();

            if (event.isCancelled()) {
                ci.cancel();
            }
        }
    }
}
