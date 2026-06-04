package dev.tensor.backend.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.TensorRedirect;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.constant.WeatherEvent;
import dev.tensor.backend.events.variable.PlayerJoinLeaveWorldEvent;
import dev.tensor.backend.events.variable.VelocityEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityOtherPlayerMP;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

@Mixin(value = World.class, priority = MixinPriority.VALUE)
public abstract class WorldMixin implements Globals {

    @Inject(method = "func_27163_E", at = @At("HEAD"), cancellable = true)
    public void tensor_onSetWeatherStrength(CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(WeatherEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(WeatherEvent.class).call(true, event -> {

            if (event.isCancelled()) {
                ci.cancel();
            }
        });
    }

    @Inject(method = "func_27166_f", at = @At("HEAD"), cancellable = true)
    public void tensor_onGetThunderStrength(float partialTicks, CallbackInfoReturnable<Float> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(WeatherEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(WeatherEvent.class).call(true, event -> {

            if (event.isCancelled()) {
                cir.setReturnValue(0.0F);
            }
        });
    }

    @Inject(method = "func_27162_g", at = @At("HEAD"), cancellable = true)
    public void tensor_onGetRainStrength(float partialTicks, CallbackInfoReturnable<Float> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(WeatherEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(WeatherEvent.class).call(true, event -> {

            if (event.isCancelled()) {
                cir.setReturnValue(0.0F);
            }
        });
    }

    @TensorRedirect
    @WrapOperation(method = "handleMaterialAcceleration", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Vec3D;lengthVector()D"))
    public double tensor_onHandleMaterialAccelerationInvokeVector(Vec3D vec3D, Operation<Double> original) {
        if (this.failsNullCheck()) {
            return vec3D.lengthVector();
        }

        if (!this.shouldCallEvent(VelocityEvent.class)) {
            return vec3D.lengthVector();
        }

        VelocityEvent event = EventCache.getCachedEvent(VelocityEvent.class).withVariables(VelocityEvent.Type.Fluid).call(true);

        if (event.isCancelled()) {
            return 0;
        }

        return vec3D.lengthVector();
    }

    @Inject(method = "playSoundAtEntity", at = @At("HEAD"), cancellable = true)
    public void tensor_onPlaySoundAtEntity(Entity entity, String name, float volume, float random, CallbackInfo ci) {
        if (this.failsNullCheck() || name == null) {
            return;
        }

        if (name.equalsIgnoreCase("mob.wolf.shake") && this.shouldCallEvent(WeatherEvent.class)) {
            EventCache.getCachedEvent(WeatherEvent.class).call(true, event -> {

                if (event.isCancelled()) {
                    ci.cancel();
                }
            });
        }
    }

    @Inject(method = "entityJoinedWorld", at = @At("HEAD"))
    public void tensor_onEntityJoinedWorld(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        if (entity instanceof EntityOtherPlayerMP && this.shouldCallEvent(PlayerJoinLeaveWorldEvent.class)) {
            EntityOtherPlayerMP otherPlayerMP = (EntityOtherPlayerMP) entity;

            EventCache.getCachedEvent(PlayerJoinLeaveWorldEvent.class).withVariables(PlayerJoinLeaveWorldEvent.Type.Join, otherPlayerMP).call(true);
        }
    }

    @Inject(method = "setEntityDead", at = @At("HEAD"))
    public void tensor_onSetEntityDead(Entity entity, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (entity instanceof EntityOtherPlayerMP && this.shouldCallEvent(PlayerJoinLeaveWorldEvent.class)) {
            EntityOtherPlayerMP otherPlayerMP = (EntityOtherPlayerMP) entity;

            EventCache.getCachedEvent(PlayerJoinLeaveWorldEvent.class).withVariables(PlayerJoinLeaveWorldEvent.Type.Kill_or_Leave, otherPlayerMP).call(true);
        }
    }
}
