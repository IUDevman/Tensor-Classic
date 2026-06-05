package dev.tensor.backend.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.TensorRedirect;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.variable.RenderOverlayEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 03-28-2026
 */

@Mixin(value = ItemRenderer.class, priority = MixinPriority.VALUE)
public final class ItemRendererMixin implements Globals {

    @Inject(method = "renderInsideOfBlock", at = @At("HEAD"), cancellable = true)
    public void tensor_onRenderInsideOfBlock(float partialTicks, int color, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(RenderOverlayEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(RenderOverlayEvent.class).withVariables(RenderOverlayEvent.Type.blocks).call(true, event -> {

            if (event.isCancelled()) {
                ci.cancel();
            }
        });
    }

    @Inject(method = "renderFireInFirstPerson", at = @At("HEAD"), cancellable = true)
    public void tensor_onRenderFireInFirstPerson(float partialTicks, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(RenderOverlayEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(RenderOverlayEvent.class).withVariables(RenderOverlayEvent.Type.fire).call(true, event -> {

            if (event.isCancelled()) {
                ci.cancel();
            }
        });
    }

    @Inject(method = "renderWarpedTextureOverlay", at = @At("HEAD"), cancellable = true)
    public void tensor_onRenderWarpedTextureOverlay(float partialTicks, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(RenderOverlayEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(RenderOverlayEvent.class).withVariables(RenderOverlayEvent.Type.water).call(true, event -> {

            if (event.isCancelled()) {
                ci.cancel();
            }
        });
    }

    @TensorRedirect
    @WrapOperation(method = "updateEquippedItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/InventoryPlayer;getCurrentItem()Lnet/minecraft/src/ItemStack;"))
    public ItemStack tensor_updateEquippedItemSlot9Fix(InventoryPlayer inventoryPlayer, Operation<ItemStack> original) {
        if (inventoryPlayer.currentItem == 9) {
            return inventoryPlayer.getStackInSlot(9);
        }

        return inventoryPlayer.getCurrentItem();
    }
}
