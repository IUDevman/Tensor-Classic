package dev.tensor.misc.gui.imp.elements;

public interface KeyPressElement {

    boolean isSearching();

    void setSearching(boolean searching);

    void onKeyPressed(char letter, int key);
}
