package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.variable.RenderBlockEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

@Mixin(value = RenderBlocks.class, priority = MixinPriority.VALUE)
public final class BlockRendererMixin implements Globals {

    @Shadow
    private boolean renderAllFaces;

    @Unique
    public boolean hasOpacity = false;

    @Inject(method = "renderBlockByRenderType", at = @At("HEAD"), cancellable = true)
    public void tensor_onRenderBlockByRenderType(Block block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        if (this.shouldCallEvent(RenderBlockEvent.class)) {
            EventCache.getCachedEvent(RenderBlockEvent.class).withVariables(block, x, y, z).call(true);
        }

        if (!this.isXrayEnabled()) {
            return;
        }

        this.renderAllFaces = false;

        if (!this.getXray().opacity.getValue()) {

            if (!this.getXray().isOnXrayList(block.blockID)) {
                cir.setReturnValue(false);
            } else {
                this.renderAllFaces = true;
            }

            return;
        }

        if (this.getXray().isOnXrayList(block.blockID) || this.getXray().isOpacityBlacklisted(block.blockID, true)) {
            this.renderAllFaces = true;
            this.getXray().setShouldOpacity(false);
        } else {
            this.getXray().setShouldOpacity(true);
            this.hasOpacity = true;
        }
    }

    @Inject(method = "renderBlockByRenderType", at = @At("TAIL"))
    public void tensor_onRenderBlockByRenderTypeTail(Block block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.isXrayEnabled()) {
            this.hasOpacity = false;
            return;
        }

        if (this.hasOpacity) {
            this.getXray().setShouldOpacity(false);
            this.hasOpacity = false;
        }
    }
}
