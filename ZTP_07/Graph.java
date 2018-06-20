import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author MM
 * @version 1.0
 */
@Stateless
public class Graph implements IGraphRemote {

    private int v = 0;
    private int e = 0;
    private Map<Integer, List<Integer>> adjacentVertexMap =
            new HashMap<>();

    /**
     * @param u first vertex
     * @param v second vertex
     */
    @Override
    public void addEdge(Integer u, Integer v) {
        addVertex(u);
        addVertex(v);
        adjacentVertexMap.get(u).add(v);
        e++;
    }

    /**
     * function adding vertex to adjacent map
     * @param v vertex
     */
    private void addVertex(Integer v){
        if (!adjacentVertexMap.containsKey(v)){
            adjacentVertexMap.put(v, new ArrayList<>());
            this.v++;
        }
    }

    /**
     * @return amount of edges
     */
    @Override
    public Integer getAmountOfEdges() {
        return e;
    }

    /**
     * @return amount of vertices
     */
    @Override
    public Integer getAmountOfVertices() {
        return v;
    }

    /**
     * @return set with all vertices in graph structure
     */
    @Override
    public Set<Integer> getVertices() {
        return adjacentVertexMap.keySet();
    }

    /**
     * @param v vertex
     * @return List with all of adjacent vertices
     */
    @Override
    public List<Integer> getAdjacentVertices(Integer v) {
        return adjacentVertexMap.get(v);
    }
}
