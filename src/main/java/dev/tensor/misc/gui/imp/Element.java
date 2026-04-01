package dev.tensor.misc.gui.imp;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.imp.Globals;

import java.awt.*;

/**
 * @author IUDevman
 * @since 05-03-2021
 */

public interface Element extends Globals {

    int getWidth();

    int getHeight();

    int getPosX();

    int getPosY();

    void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor);

    void onClick(double mouseX, double mouseY);
}
