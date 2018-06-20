import javax.ejb.Remote;

/**
 * @author MM
 * @version 1.0
 */
@Remote
public interface ICycleRemote {
    /**
     * @param graph reference to graph
     * @return amount of cycles in directed graph
     */
    int calculateAmountOfCyclesIn(IGraphRemote graph);
}
