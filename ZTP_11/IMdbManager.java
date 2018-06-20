package pl.jrj.mdb;
import javax.ejb.Remote;

/**
 * @author MM
 * @version 1.0
 */
@Remote
public interface IMdbManager {
    /**
     * @return currency code
     */
    public String currencyId();
}
