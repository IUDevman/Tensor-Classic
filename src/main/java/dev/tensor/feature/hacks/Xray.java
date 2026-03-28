package dev.tensor.feature.hacks;

import dev.tensor.backend.events.LightValueEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.Block;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

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
    public final IntegerSetting opacityValue = new IntegerSetting("Opacity Value", 50, 30, 120) {
        @Override
        public void setValue(Integer value) {
            super.setValue(value);

            if (!this.failsNullCheck() && this.isXrayEnabled()) {
                this.resetWorld();
            }
        }
    };
    public final EnumSetting mode = new EnumSetting("Mode", Mode.Ores){

        @Override
        public void setValue(Enum<?> value) {

            super.setValue(value);

            if (!this.failsNullCheck() && this.isXrayEnabled()) {
                this.resetWorld();
            }
        }
    };

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
    @EventTarget
    public void onLightValueEvent(LightValueEvent event) {
        event.cancel();
    }

    public boolean isOnXrayList(int blockID) {

        switch ((Mode) mode.getValue()) {
            case Ores:
                return this.getOres_List().contains(blockID);
            case Diamond:
                return blockID == Block.oreDiamond.blockID || blockID == Block.blockDiamond.blockID;
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

    public enum Mode {
        Ores,
        Diamond,
        Custom,
        None
    }

    private final ArrayList<Integer> ores_List = new ArrayList<>();

    private ArrayList<Integer> getOres_List() {
        if (this.ores_List.isEmpty()) {
            this.ores_List.add(Block.oreCoal.blockID);
            this.ores_List.add(Block.oreIron.blockID);
            this.ores_List.add(Block.blockSteel.blockID);
            this.ores_List.add(Block.oreGold.blockID);
            this.ores_List.add(Block.blockGold.blockID);
            this.ores_List.add(Block.oreDiamond.blockID);
            this.ores_List.add(Block.blockDiamond.blockID);
            this.ores_List.add(Block.oreRedstone.blockID);
            this.ores_List.add(Block.oreRedstoneGlowing.blockID);
            this.ores_List.add(Block.oreLapis.blockID);
            this.ores_List.add(Block.blockLapis.blockID);
        }

        return this.ores_List;
    }
}
