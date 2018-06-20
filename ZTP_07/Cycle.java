import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author MM
 * @version 1.0
 */

@Stateless
public class Cycle implements ICycleRemote {
    private Set<Integer> visited;
    private Deque<Integer> pointStack;
    private Deque<Integer> markedStack;
    private Set<Integer> markedSet;

    /**
     * Default constructor. Allocate class structure
     */
    public Cycle() {
        visited = new HashSet<>();
        pointStack = new LinkedList<>();
        markedStack = new LinkedList<>();
        markedSet = new HashSet<>();
    }

    /**
     * @param graph reference to graph object
     * @return List of vertex which make a cycles
     */
    public List<List<Integer>> findAllSimpleCycles(IGraphRemote graph) {
        List<List<Integer>> result = new ArrayList<>();
        for(Integer i : graph.getVertices()) {
            findAllSimpleCycles(i, i, graph, result);
            visited.add(i);
            while(!markedStack.isEmpty()) {
                markedSet.remove(markedStack.pollFirst());
            }
        }
        return result;
    }

    /**
     * @param start start vertex
     * @param current current vertex
     * @param graph reference to graph
     * @param result List in which stored is vertex
     * @return true if cycle exist otherwise false
     */
    private boolean findAllSimpleCycles(Integer start, Integer current, IGraphRemote graph, List<List<Integer>> result) {
        boolean hasCycle = false;
        pointStack.offerFirst(current);
        markedSet.add(current);
        markedStack.offerFirst(current);

        for (Integer w : graph.getAdjacentVertices(current)) {
            if (visited.contains(w)) {
                continue;
            } else if (w.equals(start)) {
                hasCycle = true;
                pointStack.offerFirst(w);
                List<Integer> cycle = new ArrayList<>();
                Iterator<Integer> itr = pointStack.descendingIterator();
                while(itr.hasNext()) {
                    cycle.add(itr.next());
                }
                pointStack.pollFirst();
                result.add(cycle);
            } else if (!markedSet.contains(w)) {
                hasCycle = findAllSimpleCycles(start, w, graph, result) || hasCycle;
            }
        }

        if (hasCycle) {
            while(!markedStack.peekFirst().equals(current)) {
                markedSet.remove(markedStack.pollFirst());
            }
            markedSet.remove(markedStack.pollFirst());
        }

        pointStack.pollFirst();
        return hasCycle;
    }

    /**
     * @param graph Reference to graph
     * @return Amount of cycles in directed Graph
     */
    @Override
    public int calculateAmountOfCyclesIn(IGraphRemote graph) {
        List<List<Integer>> result = findAllSimpleCycles(graph);
        return result.size();
    }
}
