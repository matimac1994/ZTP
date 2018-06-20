package pl.jrj.data;
import javax.ejb.Remote;

/**
 *
 * @author MM
 */
@Remote
public interface IDataMonitor {
    public boolean hasNext();
    public double next();
}
