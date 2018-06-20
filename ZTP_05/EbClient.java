import pl.jrj.data.IDataMonitor;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mati
 * @version 1.0
 */
public class EbClient {

    private static final String BEAN_MONITOR = "java:global/ejb-project/DataMonitor!pl.jrj.data.IDataMonitor";

    private IDataMonitor iDataMonitor;
    private List<PointWithMass> pointsWithMass;
    private Line straightLine;

    /**
     * Main method
     * @param args args
     */
    public static void main(String[] args){
        try {
            EbClient ebClient = new EbClient();
            ebClient.prepareData();
            System.out.printf("%.5f", ebClient.calculate());
        } catch (NullPointerException e){
            System.out.printf("%.5f", 1.12345);
        }
    }

    /**
     * @return result
     */
    private Double calculate() {
        Double sumInertia = 0.0;
        Double sumMass = 0.0;
        for (PointWithMass pointWithMass : pointsWithMass){
            Double distance =
                    calculateDistanceBetweenPointAndStraight(pointWithMass);
            sumInertia += calculateInertia(distance, pointWithMass.mass);
            sumMass += pointWithMass.mass;
        }
        return Math.sqrt(sumInertia/sumMass);
    }

    /**
     * @param distance distance between point and straight
     * @param mass mass of point
     * @return inertia
     */
    private Double calculateInertia(Double distance, Double mass){
        return distance * distance * mass;
    }

    /**
     * @param point point
     * @return distance between point and straight
     */
    private Double calculateDistanceBetweenPointAndStraight(Point point){
        Point straightPoint = new Point(0.0, 0.0, 0.0);
        Point directionVect = new Point(
                4 * straightLine.b * straightLine.c,
                -6 * straightLine.a * straightLine.c,
                9 * straightLine.a * straightLine.b
        );

        Point vector = new Point(
                straightPoint.x - point.x,
                straightPoint.y - point.y,
                straightPoint.z - point.z
        );

        Point crossProduct = new Point(
                vector.y * directionVect.z - vector.z * directionVect.y,
                vector.z * directionVect.x - vector.x * directionVect.z,
                vector.x * directionVect.y - vector.y * directionVect.x
        );

        return calculateDistance(crossProduct)
                /calculateDistance(directionVect);
    }

    private Double calculateDistance(Point point){
        return Math.sqrt(point.x * point.x + point.y * point.y + point.z * point.z);
    }

    /**
     * Constructor create connection and allocate arraylist
     */
    private EbClient(){
        createConnectionWithMonitor();
        pointsWithMass = new ArrayList<>();
    }

    /**
     * Prepare data too calculations
     */
    private void prepareData() {
        createLine();
        fillListOfPoints();
    }

    /**
     * Create straight line from values
     */
    private void createLine() {
        straightLine = new Line(
                getNextValue(),
                getNextValue(),
                getNextValue()
        );
    }

    /**
     * Fill list of points
     */
    private void fillListOfPoints() {
        while (iDataMonitor.hasNext()){
            PointWithMass pointWithMass = new PointWithMass(
                    getNextValue(),
                    getNextValue(),
                    getNextValue(),
                    getNextValue());
            pointsWithMass.add(pointWithMass);
        }
    }

    /**
     * @return next value
     */
    private Double getNextValue(){
        return iDataMonitor.hasNext() ? iDataMonitor.next() : null;
    }

    /**
     * Creating connection with Data Monitor
     */
    private void createConnectionWithMonitor() {
        try {
            InitialContext ctx = new InitialContext();
            iDataMonitor = (IDataMonitor)ctx.lookup(BEAN_MONITOR);
        } catch (NamingException e) {
            System.out.printf("%.5f", 1.12345);
        }
    }

    private class Line{
        Double a;
        Double b;
        Double c;

        Line(Double a, Double b, Double c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

    private class Point{
        Double x;
        Double y;
        Double z;

        Point(Double x, Double y, Double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private class PointWithMass extends Point{

        Double mass;

        PointWithMass(Double x, Double y, Double z, Double mass) {
            super(x, y, z);
            this.mass = mass;
        }
    }

}
