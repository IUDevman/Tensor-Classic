package dev.tensor.misc.helpers.text;

import dev.tensor.misc.imp.render.Render2D;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hoosiers
 * @since 09-09-2026
 */

public final class StringRenderer implements Render2D {

    /**
     * Add the '₿' symbol to a string to reset the color back to the initial.
     * It will render the first block (before the '₿') in the current color, then swap to the initial.
     */
    public static final String resetToInitial = "₿";

    public static StringRenderer newInstance(Color initialColor) {
        return new StringRenderer(initialColor);
    }

    private final LinkedHashMap<String, Color> splitText = new LinkedHashMap<>();

    private final Color initialColor;

    private Color setColor;

    private StringRenderer(Color initialColor) {
        this.initialColor = initialColor;
        this.setColor = this.initialColor;
    }

    public StringRenderer addText(String text) {
        if (!text.contains(resetToInitial)) {
            this.processInTextColorAndEnsureUnique(text, this.setColor);
        } else {
            String[] split = text.split(resetToInitial);

            for (int i = 0; i < split.length; i++) {
                if (i == 0 && split.length > 1) {
                    this.processInTextColorAndEnsureUnique(split[i], this.setColor);

                } else {
                    this.processInTextColorAndEnsureUnique(split[i], this.initialColor);
                }
            }

            this.changeColor(this.initialColor);
        }

        return this;
    }

    private void processInTextColorAndEnsureUnique(String text, Color color) {
        if (this.splitText.containsKey(text)) {

            text = resetToInitial + text;

            if (this.splitText.containsKey(text)) {
                this.processInTextColorAndEnsureUnique(text, color);

            } else {
                this.splitText.put(text, color);
            }

        } else {
            this.splitText.put(text, color);
        }
    }

    public StringRenderer switchColor(Color newColor) {
        this.changeColor(newColor);

        return this;
    }

    public StringRenderer resetColor() {
        this.changeColor(this.initialColor);

        return this;
    }

    private void changeColor(Color newColor) {
        this.setColor = newColor;
    }

    public void draw(int x, int y) {
        if (this.splitText.isEmpty()) {
            return;
        }

        AtomicInteger offsetX = new AtomicInteger(0);

        this.splitText.forEach((string, color) -> {

            if (string.contains(resetToInitial)) {
                string = string.replace(resetToInitial, "");
            }

            this.drawString(string, x + offsetX.get(), y, color.getRGB());
            offsetX.set(offsetX.get() + this.getFontRenderer().getStringWidth(string) + 1);
        });
    }

    public void drawCenter(int x, int y) {
        AtomicInteger offsetX = new AtomicInteger(0);

        this.splitText.keySet().forEach(string -> {

            if (string.contains(resetToInitial)) {
                string = string.replace(resetToInitial, "");
            }

            offsetX.set(offsetX.get() + this.getFontRenderer().getStringWidth(string) + 1);
        });

        offsetX.set(offsetX.get() - 1);
        offsetX.set(offsetX.get() / 2);

        this.draw(x - offsetX.get(), y);
    }
}
