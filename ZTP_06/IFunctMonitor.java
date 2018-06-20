package pl.jrj.fnc;
import javax.ejb.Remote;

/**
 * @author MM
 * @version 1.0
 */
@Remote
public interface IFunctMonitor {

    /**
     * @param x x
     * @param y x
     * @return f(x,y)
     */
    public double f( double x, double y );
}