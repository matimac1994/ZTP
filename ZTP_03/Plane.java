import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author MM
 */

public class Plane extends HttpServlet {

    private static final long serialVersionUID = 16446L;

    private Map<Float, List<Point>> coord;
    private String query = "SELECT x, y, z FROM Otable";
    private String dbUrl;

    /**
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        dbUrl = request.getParameter("db");

        PrintWriter out = response.getWriter();
        out.printf("%.5f", getResult());
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * @return result
     */
    private Double getResult(){

        getDataFromDatabase();

        return getMaximumArea();
    }

    /**
     * Create connection to database and call getDataFromResultSet
     */
    private void getDataFromDatabase() {
        Connection connection;
        Statement statement;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(dbUrl);
            statement = connection.createStatement();

            getDataFromResultSet(statement.executeQuery(query));

            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param resultSet result from sql query statement
     * @throws SQLException when ocuur sql exception
     */
    private void getDataFromResultSet(ResultSet resultSet) throws SQLException {
        coord = new HashMap<>();
        while (resultSet.next()){
            Point point = new Point(resultSet.getFloat("x"), resultSet.getFloat("y"));
            if (coord.containsKey(resultSet.getFloat("z"))){
                List<Point> points = coord.get(resultSet.getFloat("z"));
                points.add(point);
            } else {
                List<Point> points = new LinkedList<>();
                points.add(point);
                coord.put(resultSet.getFloat("z"), points);
            }
        }
    }

    /**
     * @return maximum area
     */
    private Double getMaximumArea() {
        Double max = 0.0;

        for (Map.Entry<Float, List<Point>> reg : coord.entrySet()){
            Double current = getAreaForRegion(reg.getValue());

            if (current > max) max = current;
        }

        return max;
    }

    /**
     * @param region list of points
     * @return area for region
     */
    private Double getAreaForRegion(List<Point> region) {
        Double area = 0.0;

        List<Point> convexHull = createHullFromArea(region);
        Point firstPoint = convexHull.get(0);

        System.out.println("levelxxx");
        convexHull.forEach((l) -> System.out.println(l));
        for (int i = 1; i < convexHull.size() - 1; i++) {
            area += getTriangArea(firstPoint, convexHull.get(i),
                    convexHull.get(i + 1));
        }

        return area;
    }

    /**
     * @param point1 point 1
     * @param point2 point 2
     * @param point3 point 3
     * @return triangle area
     */
    private Double getTriangArea(Point point1, Point point2, Point point3) {
        Double a = point1.distanceFromPoint(point2);
        Double b = point2.distanceFromPoint(point3);
        Double c = point3.distanceFromPoint(point1);
        Double result = (a + b + c) / 2.0;

        return Math.sqrt(result * (result - a) * (result - b) * (result - c));
    }

    /**
     * @param region list of points
     * @return created hull
     */
    private List<Point> createHullFromArea(List<Point> region) {
        region.sort(new PointComparator());
        List<Point> lowerHull = buildHullFromListOfPoints(region);

        Collections.reverse(region);
        List<Point> upperHull = buildHullFromListOfPoints(region);

        upperHull.addAll(lowerHull);

        return upperHull;
    }

    /**
     * @param region list of points
     * @return hull
     */
    private List<Point> buildHullFromListOfPoints(List<Point> region) {
        List<Point> hull = new ArrayList<>();

        for (Point point : region) {
            while (hull.size() > 1 &&
                    getCrossOfVectors(
                            hull.get(hull.size() - 2),
                            hull.get(hull.size() - 1), point) <= 0) {

                hull.remove(hull.size() - 1);
            }
            hull.add(point);
        }

        return hull;
    }

    /**
     * @param currentPoint current point
     * @param penultimatePoint penultimate point
     * @param lastPoint last point
     * @return cross product of vectors
     */
    private float getCrossOfVectors(Point currentPoint, Point penultimatePoint, Point lastPoint) {
        return (penultimatePoint.getX() - currentPoint.getX()) *
                (lastPoint.getY() - currentPoint.getY()) -
                (penultimatePoint.getY() - currentPoint.getY()) *
                        (lastPoint.getX() - currentPoint.getX());
    }

    /**
     * represents comparator of two points
     */
    private class PointComparator implements Comparator<Point>{

        @Override
        public int compare(Point point1, Point point2) {
            if (point1.getX() < point2.getX()) {
                return -1;
            }
            if (point1.getX() > point2.getX()) {
                return 1;
            }
            return Float.compare(point1.getY(), point2.getY());
        }
    }

    /**
     * Class representing Point
     */
    private class Point{
        private float x;
        private float y;

        Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        /**
         * @param point point from which will be calculate distance
         * @return distance between current and param point
         */
        Double distanceFromPoint(Point point){
            double px = point.getX() - this.getX();
            double py = point.getY() - this.getY();
            return Math.sqrt(px * px + py * py);
        }

        public Point() {}

        public float getX() { return x; }

        public void setX(float x) { this.x = x; }

        public float getY() { return y; }

        public void setY(float y) { this.y = y; }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
