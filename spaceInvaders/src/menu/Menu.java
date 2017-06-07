package menu;

import animations.Animation;

/**
 * A Menu interface.
 *
 * @param <T> is void.
 * @author Nir Dunetz and Haim Gil.
 */
public interface Menu<T> extends Animation {

    /**
     * Adding selection to the menu.
     *
     * @param key       is the key that need to be pressed to turn off the animation.
     * @param message   is the message selection.
     * @param returnVal is the animation.
     */
    void addSelection(String key, String message, T returnVal);

    /**
     * @return the status of the animation.
     */
    T getStatus();

    /**
     * Setting the animation after the key was pressed.
     */
    void setAnimation();

}