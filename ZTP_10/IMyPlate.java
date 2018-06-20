import java.util.List;

/**
 * @author MM
 * @version 1.0
 */
public interface IMyPlate {
    /**
     * @param plateCosts list of plate Cost
     * @return cost of cuts
     */
    Double calculateCostOfCuts(List<PlateCost> plateCosts);
}
