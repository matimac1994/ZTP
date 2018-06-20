import javax.ejb.Remote;

/**
 * @author MM
 * @version 1.0
 */

@Remote
public interface IControlRemote {
    /**
     * start counting
     */
    public void start();

    /**
     * stop counting
     */
    public void stop();

    /**
     * @param i incremented value
     */
    public void increment(int i);

    /**
     * @return counter
     */
    public int counter();

    /**
     * @return errors
     */
    public int errors();
}