import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.InsufficientDataException;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

/**
 * @author Mati
 * @version 1.0
 */
public class Gauss extends HttpServlet {

    private static final long serialVersionUID = 1446L;

    private List<Double> points = new ArrayList<>();
    private boolean isAggregateData = true;

    /**
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException exception which server cam throw
     * @throws IOException exception which server cam throw
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        aggregateData(request);
    }

    /**
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException exception which server cam throw
     * @throws IOException exception which server cam throw
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        isAggregateData = false;

        PrintWriter printWriter = response.getWriter();

        printWriter.printf("%.2f", calculate());
        response.setStatus(HttpServletResponse.SC_OK);

        isAggregateData = true;

    }

    /**
     * Function aggregate data to list
     * @param request HttpServletRequest
     */
    private void aggregateData(HttpServletRequest request) {
        try {
            if (isAggregateData){
                String n = request.getParameter("n");
                if (n != null && !n.equals(""))
                    points.add(Double.parseDouble(n));
            }
        } catch (NumberFormatException e){}
    }

    /**
     * @return significant level of normal distribution test
     */
    private double calculate() {
        double[] arr = points.stream().mapToDouble(Double::doubleValue).toArray();

        RealDistribution nDistribution = new NormalDistribution(calculateAverage(), calculateStandardDeviation(arr));

        KolmogorovSmirnovTest kolmogorovSmirnovTest = new KolmogorovSmirnovTest();
        double result = 0.0;
        try {
           result = 1.0 - kolmogorovSmirnovTest
                    .cdf(kolmogorovSmirnovTest
                            .kolmogorovSmirnovStatistic(nDistribution, arr), arr.length);
        } catch (InsufficientDataException e){}

        return result;
    }

    /**
     * @return average of points array
     */
    private double calculateAverage(){
        double average = 0.0;
        OptionalDouble averageOptional = points.stream().mapToDouble(a -> a).average();
        if (averageOptional.isPresent())
            average = averageOptional.getAsDouble();
        return average;
    }

    /**
     * @param arr array of points
     * @return standard deviation of array
     */
    private double calculateStandardDeviation(double[] arr){
        StandardDeviation standardDeviation = new StandardDeviation();
        return standardDeviation.evaluate(arr);
    }
}
