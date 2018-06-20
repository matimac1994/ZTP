import javax.ejb.Remote;
import java.util.List;
import java.util.Set;

/**
 * @author MM
 * @version 1.0
 */
@Remote
public interface IGraphRemote {
    /**
     * @param u first vertex
     * @param v second vertex
     */
    void addEdge(Integer u, Integer v);

    /**
     * @return amount of edges
     */
    Integer getAmountOfEdges();

    /**
     * @return amount of vertices
     */
    Integer getAmountOfVertices();

    /**
     * @param v vertex
     * @return List with all of adjacent vertices
     */
    List<Integer> getAdjacentVertices(Integer v);

    /**
     * @return set with all vertices in graph structure
     */
    Set<Integer> getVertices();
}
