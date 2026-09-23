package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.backend.events.variable.RenderSkyEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.EventPriority;
import dev.tensor.misc.helpers.block.BlockList;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BlockListSetting;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.Block;
import org.lwjgl.input.Keyboard;

/**
 * @author Hoosiers
 * @since 03-26-2026
 */

@Hack.Info(name = "Xray", category = Category.Render, bind = Keyboard.KEY_X, description = "Allows you to see defined blocks (such as ores) through the world.")
public final class Xray extends Hack {

    public final BooleanSetting clouds = new BooleanSetting("Render Clouds", true, "Render clouds when Xray is enabled.");
    public final BooleanSetting opacity = new BooleanSetting("Opacity", true, "Enable to render the world at a lower defined opacity.") {

        @Override
        public void setValue(Boolean value) {
            super.setValue(value);

            if (!this.failsNullCheck() && this.isXrayEnabled()) {
                this.resetWorld();
            }
        }
    };
    public final IntegerSetting opacityValue = new IntegerSetting("Opacity Value", 100, 50, 200) {

        @Override
        public void setValue(Integer value) {
            super.setValue(value);

            if (!this.failsNullCheck() && this.isXrayEnabled()) {
                this.resetWorld();
            }
        }
    };
    public final BooleanSetting fluids = new BooleanSetting("Fluids", true, "Enable to render fluids. Is automatically enabled if rendering the world with opacity.") {

        @Override
        public void setValue(Boolean value) {
            super.setValue(value);

            if (!this.failsNullCheck() && this.isXrayEnabled()) {
                this.resetWorld();
            }
        }
    };
    public final EnumSetting mode = new EnumSetting("Mode", Mode.Ores, new String[] {
            "This is where you set the list of blocks to render.",
            "Ores® List of ore blocks.",
            "Glowstone® Only glowstone.",
            "Diamond® Only diamond ore.",
            "Utility® Common player made blocks and ores.",
            "Custom® Defined list in the setting below.",
            "None® Render just a blank world."
    }) {

        @Override
        public void setValue(Enum<?> value) {

            super.setValue(value);

            if (!this.failsNullCheck() && this.isXrayEnabled()) {
                this.resetWorld();
            }
        }
    };
    public final BlockListSetting custom = new BlockListSetting("Custom Block List", new BlockList(BlockList.CUSTOM()), "Set types of blocks to render.") {

        @Override
        public void hasChanged() {

            if (!this.failsNullCheck() && this.isXrayEnabled() && this.getXray().mode.getValue().equals(Mode.Custom)) {
                this.resetWorld();
            }
        }
    };

    private final BlockList ORES = new BlockList(BlockList.ORES());
    private final BlockList DIAMOND = new BlockList(BlockList.DIAMOND());
    private final BlockList GLOWSTONE = new BlockList(BlockList.GLOWSTONE());
    private final BlockList UTILITY = new BlockList(BlockList.UTILITY());
    private final BlockList OPACITY_BLOCKED = new BlockList(BlockList.WATER_LAVA());

    private boolean shouldOpacity = false;

    private boolean shouldTriggerLightMapUpdate = false;

    @Override
    public void onEnable(boolean isNull) {
        if (!isNull) {
            this.fillLightMap(false);
            this.resetWorld();
            return;
        }

        this.shouldTriggerLightMapUpdate = true;
    }

    @Override
    public void onDisable(boolean isNull) {
        if (!isNull) {
            Fullbright fullbright = this.getHackManager().getHack(Fullbright.class);

            if (fullbright != null && fullbright.isEnabled()) {

                if (fullbright.ugandaMode.getValue()) {
                    this.fillLightMap(true);
                }

            } else {
                this.resetLightMap();
            }

            this.resetWorld();
        }

        this.shouldTriggerLightMapUpdate = false;
    }

    @Override
    public void onClientTick() {
        if (this.shouldTriggerLightMapUpdate) {
            this.fillLightMap(false);
            this.resetWorld();
            this.shouldTriggerLightMapUpdate = false;
        }
    }

    @SuppressWarnings("unused")
    private final EventAction<ChangeWorldEvent> changeWorldEventAction = this.addHackEventAction(new EventAction<>(ChangeWorldEvent.class, EventPriority.Highest, event -> {
        this.fillLightMap(false);
        this.resetWorld();
    }));

    @SuppressWarnings("unused")
    private final EventAction<RenderSkyEvent> renderSkyEventAction = this.addHackEventAction(new EventAction<>(RenderSkyEvent.class, event -> {
        if (event.type.equals(RenderSkyEvent.Type.FOG_FLUIDS)) {
            event.cancel();
        }
    }));

    public boolean isOnXrayList(int blockID) {

        if (!this.fluids.getValue() || this.opacity.getValue()) {

            if (this.OPACITY_BLOCKED.containsBlock(blockID)) {
                return false;
            }
        }

        switch ((Mode) mode.getValue()) {
            case Ores:
                return this.ORES.containsBlock(blockID);
            case Diamond:
                return this.DIAMOND.containsBlock(blockID);
            case Glowstone:
                return this.GLOWSTONE.containsBlock(blockID);
            case Utility:
                return this.UTILITY.containsBlock(blockID);
            case Custom:
                return this.custom.containsBlock(blockID);
            default:
                return false;
        }
    }

    public boolean shouldRenderOpacity() {
        if (!this.opacity.getValue()) {
            return false;
        }

        return this.shouldOpacity;
    }

    public void setShouldOpacity(boolean shouldOpacity) {
        this.shouldOpacity = shouldOpacity;
    }

    //General idea:
    //Applying opacity to water/lava messes up their texture.
    //We always want to generate lava as an xray block with opacity.
    //In order to prevent the weird glitch where only the top face of the top block
    //is rendered.
    //Water is on a different render pass, so we want to exclude water in some cases.
    public boolean isOpacityBlacklisted(int blockID, boolean lavaOnly) {
        if (lavaOnly) {
            return blockID == Block.lavaStill.blockID || blockID == Block.lavaMoving.blockID;
        }

        return this.OPACITY_BLOCKED.containsBlock(blockID);
    }

    //Opacity breaks if sign blocks are moved to the first render pass
    public boolean isBrokenOpacity(int blockID) {
        return blockID == Block.signPost.blockID || blockID == Block.signWall.blockID;
    }

    public enum Mode {
        Ores,
        Glowstone,
        Diamond,
        Utility,
        Custom,
        None
    }
}
