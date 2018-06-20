import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * @author MM
 * @version 1.0
 */
@Path("exchangeRate")
public class ExchRates {

    private static final String MDB_JNDI =
            "java:global/mdb-project/MdbManager!pl.jrj.mdb.IMdbManager";

    private pl.jrj.mdb.IMdbManager iMdbManager;

    /**
     * @param currencyCode code from url request
     * @return current exchange data
     */
    @GET
    @Path("/{currCode}")
    @Produces("text/plain;charset=utf-8")
    public String getCurrencyByCode(
            @PathParam("currCode") String currencyCode
    ){
        try {
            NbpUtil nbpUtil = new NbpUtil();

            registerManager();

            String remoteCurrencyCode = getRemoteCurrencyCode();

            return nbpUtil
                    .calculateRateByCurrency(
                            currencyCode, remoteCurrencyCode
                    );
        } catch (NamingException e){
            return "0.0000";
        }
    }

    /**
     * @return remote currency code
     */
    private String getRemoteCurrencyCode() {
        if (iMdbManager != null){
            return iMdbManager.currencyId();
        }
        return "";
    }

    /**
     * Connect interface with implementation
     * @throws NamingException when problem with connection
     */
    private void registerManager() throws NamingException {
        InitialContext context = new InitialContext();
        iMdbManager =
                (pl.jrj.mdb.IMdbManager) context.lookup(
                MDB_JNDI);
    }


}
