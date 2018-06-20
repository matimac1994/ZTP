import pl.jrj.fnc.IFunctMonitor;

import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author MM
 * @version 1.0
 */
@Stateless
public class IBean implements IBeanRemote {

    private IFunctMonitor iFunctMonitor;

    @Override
    public String solve(double a, double b, int n) {
        try {
            iFunctMonitor = getFunctMonitor();
            return String.format("%."+n+"f", calculate(a,b));
        } catch (NamingException e) {
            return String.format("%."+n+"f", 1.0);
        }
    }

    /**
     * @param a upper
     * @param b upper
     * @return result
     */
    private double calculate(double a, double b) {
        int steps = 500;
        return calculateIntegral(a, b, steps);
    }

    /**
     * @param a upper
     * @param b upper
     * @param steps amount of steps
     * @return result
     */
    private double calculateIntegral(double a, double b, int steps){
        double xStep = a/steps;
        double yStep = b/steps;
        double result = 0.0;

        for (int i = 0; i < steps; i++) {
            for (int j = 0; j < steps; j++) {
                double heightOfTrapezium = iFunctMonitor.f((xStep * i), (yStep * j));
                heightOfTrapezium += iFunctMonitor.f((xStep * (i + 1)), (yStep * j));
                heightOfTrapezium += iFunctMonitor.f((xStep * (i + 1)), (yStep * (j + 1)));
                heightOfTrapezium += iFunctMonitor.f((xStep * i), (yStep * (j + 1)));
                heightOfTrapezium /= 4;

                result += xStep * yStep * heightOfTrapezium;
            }
        }
        return result;
    }

    /**
     * @return IFunctMonitor instance or
     * @throws NamingException if wrong name
     */
    private IFunctMonitor getFunctMonitor() throws NamingException {
        InitialContext initialContext = new InitialContext();
        return (IFunctMonitor) initialContext
                .lookup("java:global/ejb-project/FunctMonitor!pl.jrj.fnc.IFunctMonitor");
    }
}
