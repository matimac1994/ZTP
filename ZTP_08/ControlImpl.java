import javax.ejb.Stateful;

/**
 * @author MM
 * @version 1.0
 */
@Stateful
public class ControlImpl implements IControlRemote {
    private boolean isTurnOn = false;
    private int counter = 0;
    private int errors = 0;

    /**
     * start counting
     */
    @Override
    public void start() {
        if (this.isTurnOn) {
            ++this.errors;
        } else {
            this.isTurnOn = true;
        }
    }

    /**
     * stop counting
     */
    @Override
    public void stop() {
        if (this.isTurnOn) {
            this.isTurnOn = false;
        } else {
            ++this.errors;
        }
    }

    /**
     * @param i incremented value
     */
    @Override
    public void increment(int i) {
        if (this.isTurnOn) {
            this.counter += i;
        } else {
            ++this.errors;
        }
    }

    /**
     * @return counter
     */
    @Override
    public int counter() {
        return this.counter;
    }

    /**
     * @return errors
     */
    @Override
    public int errors() {
        return this.errors;
    }
}
