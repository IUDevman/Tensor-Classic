package dev.tensor.misc.gui.imp.elements;

/**
 * @author Hoosiers
 * @since 03/22/2026
 */

public interface KeyPressElement {

    boolean isSearching();

    void setSearching(boolean searching);

    void onKeyPressed(char letter, int key);
}
