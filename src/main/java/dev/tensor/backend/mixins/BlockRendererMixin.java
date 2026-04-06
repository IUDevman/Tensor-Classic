package dev.tensor.backend.mixins;

import dev.tensor.backend.events.RenderBlockEvent;
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

@Mixin(value = RenderBlocks.class, priority = Integer.MAX_VALUE)
public final class BlockRendererMixin implements Globals {

    @Shadow
    private boolean renderAllFaces;

    @Unique
    public boolean pre = false;

    @Inject(method = "renderBlockByRenderType", at = @At("HEAD"), cancellable = true)
    public void onRenderBlockByRenderType(Block i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        if (this.isSearchEnabled()) {
            RenderBlockEvent event = new RenderBlockEvent(new BlockCoords(i, j, k, par4));
            this.getEventManager().call(event);
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

        if (this.getXray().isOnXrayList(i.blockID)) {
            this.renderAllFaces = true;
            this.getXray().setShouldOpacity(false);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        } else {
            this.getXray().setShouldOpacity(true);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.pre = true;
        }
    }

    @Inject(method = "renderBlockByRenderType", at = @At("TAIL"), cancellable = true)
    public void onRenderBlockByRenderTypeTail(Block i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.isXrayEnabled()) {
            this.pre = false;
            return;
        }

        if (this.pre) {
            GL11.glDisable(GL11.GL_BLEND);
            this.getXray().setShouldOpacity(false);
            this.pre = false;
        } else {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }
    }
}
