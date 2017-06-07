package indicators;

/**
 * A Counter class.
 *
 * @author Nir Dunetz and Haim Gil.
 */
public class Counter {
    // Member.
    private int value;

    /**
     * Constructor.
     *
     * @param value Is a Value to be entered .
     */
    public Counter(int value) {
        this.value = value;
    }

    /**
     * Constructor, initialize value to zero.
     */
    public Counter() {
        this.value = 0;
    }

    /**
     * Add a number to current count.
     *
     * @param num Is a number to be added to value.
     */
    public void increase(int num) {
        this.value += num;
    }

    /**
     * Subtract a number from current count.
     *
     * @param num Is a number to be subtract from value.
     */
    public void decrease(int num) {
        this.value -= num;
    }

    /**
     * Get the current count.
     *
     * @return value of the Counter.
     */
    public int getValue() {
        return this.value;
    }
}