package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.LightValueEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventConstantCancellable;
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

@Hack.Info(name = "Xray", category = Category.Render, bind = Keyboard.KEY_X)
public final class Xray extends Hack {

    public final BooleanSetting opacity = new BooleanSetting("Opacity", false) {

        @Override
        public void setValue(Boolean value) {
            super.setValue(value);

            if (!this.failsNullCheck() && this.isXrayEnabled()) {
                this.resetWorld();
            }
        }
    };
    public final IntegerSetting opacityValue = new IntegerSetting("Opacity Value", 100, 30, 200) {
        @Override
        public void setValue(Integer value) {
            super.setValue(value);

            if (!this.failsNullCheck() && this.isXrayEnabled()) {
                this.resetWorld();
            }
        }
    };
    public final BooleanSetting fluids = new BooleanSetting("Fluids", true) {

        @Override
        public void setValue(Boolean value) {
            super.setValue(value);

            if (!this.failsNullCheck() && this.isXrayEnabled()) {
                this.resetWorld();
            }
        }
    };
    public final EnumSetting mode = new EnumSetting("Mode", Mode.Ores) {

        @Override
        public void setValue(Enum<?> value) {

            super.setValue(value);

            if (!this.failsNullCheck() && this.isXrayEnabled()) {
                this.resetWorld();
            }
        }
    };
    public final BlockListSetting custom = new BlockListSetting("Custom Block List", new BlockList(BlockList.CUSTOM())) {

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
    private final BlockList OPACITY_BLOCKED = new BlockList(BlockList.OPACITY_BLACKLIST());

    private boolean shouldOpacity = false;

    @Override
    public void onEnable(boolean isNull) {
        if (!isNull) {
            this.resetWorld();
        }
    }

    @Override
    public void onDisable(boolean isNull) {
        if (!isNull) {
            this.resetWorld();
        }
    }

    @SuppressWarnings("unused")
    EventAction<LightValueEvent> lightValueEventAction = this.addEventAction(new EventAction<>(LightValueEvent.class, EventConstantCancellable::cancel));

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

    public boolean isOpacityBlacklisted(int blockID) {
        return this.OPACITY_BLOCKED.containsBlock(blockID);
    }

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
