import javax.ejb.Remote;

/**
 * @author MM
 * @version 1.0
 */
@Remote
public interface IBeanRemote {
    /**
     * @param a upper
     * @param b upper
     * @param n n size
     * @return result
     */
    public String solve(double a, double b, int n);
}
