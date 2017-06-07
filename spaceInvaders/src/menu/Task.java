package menu;

/**
 * A Task interface.
 *
 * @param <T> is void.
 * @author Nir Dunetz and Haim Gil.
 */
public interface Task<T> {
    /**
     * @return null.
     */
    T run();
}
