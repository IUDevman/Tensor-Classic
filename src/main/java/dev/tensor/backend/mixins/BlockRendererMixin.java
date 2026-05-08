package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.variable.RenderBlockEvent;
import dev.tensor.misc.helpers.block.BlockCoords;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;
import org.lwjgl.opengl.GL11;
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
    public boolean pre = false;

    @Inject(method = "renderBlockByRenderType", at = @At("HEAD"), cancellable = true)
    public void tensor_onRenderBlockByRenderType(Block i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        if (this.isSearchEnabled()) {
            EventCache.getCachedEvent(RenderBlockEvent.class).withVariables(new BlockCoords(i, j, k, par4)).call();
        }

        if (!this.isXrayEnabled()) {
            return;
        }

        this.renderAllFaces = false;

        if (!this.getXray().opacity.getValue()) {

            if (!this.getXray().isOnXrayList(i.blockID)) {
                cir.setReturnValue(false);
            } else {
                this.renderAllFaces = true;
            }

            return;
        }

        if (this.getXray().isOnXrayList(i.blockID) || this.getXray().isOpacityBlacklisted(i.blockID, true)) {
            this.renderAllFaces = true;
            this.getXray().setShouldOpacity(false);
        } else {
            this.getXray().setShouldOpacity(true);
            this.pre = true;
        }
    }

    @Inject(method = "renderBlockByRenderType", at = @At("TAIL"), cancellable = true)
    public void tensor_onRenderBlockByRenderTypeTail(Block i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.isXrayEnabled()) {
            this.pre = false;
            return;
        }

        if (this.pre) {
            this.getXray().setShouldOpacity(false);
            this.pre = false;
        }
    }
}
