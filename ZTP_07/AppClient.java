import javax.ejb.EJB;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author MM
 * @version 1.0
 */
public class AppClient {

    static final long serialVersionUID = 123L;

    @EJB IGraphRemote iGraphRemote = new Graph();
    @EJB ICycleRemote iCycleRemote = new Cycle();

    /**
     * Main static function
     * @param args args
     */
    public static void main(String[] args){
        AppClient appClient = new AppClient();
        System.out.println(appClient.calculateCycles(args));
    }

    /**
     * @param args args passed into program
     * @return amount of cycles in directed graph
     */
    private int calculateCycles(String[] args) {
        try {
            readDataFromFile(args[0]);
        } catch (FileNotFoundException e) {
            return 0;
        }
        return iCycleRemote.calculateAmountOfCyclesIn(iGraphRemote);
    }

    /**
     * @param filePath path to file with data
     * @throws FileNotFoundException when file not found
     */
    private void readDataFromFile(String filePath)
            throws FileNotFoundException {
        List<Integer> values = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filePath));
        while (scanner.hasNextInt()){
            values.add(scanner.nextInt());
        }

        for (int i = 0; i < values.size(); i += 2){
            iGraphRemote.addEdge(values.get(i), values.get(i + 1));
        }
    }
}
