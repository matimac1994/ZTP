import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author MM
 * @version 1.0
 */
@WebServlet("/Calc")
public class Calc extends HttpServlet{

    private static final long serialVersionUID = 123;

    @EJB
    private IBeanRemote iBeanRemote;

    /**
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException exception
     */
    private void processRequest(HttpServletRequest req,
                                HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain;charset=UTF-8");

        PrintWriter out = resp.getWriter();
        try {
            int n = Integer.valueOf(getParameterFromRequest(req, "n"));
            double a = Double.valueOf(getParameterFromRequest(req, "a"));
            double b = Double.valueOf(getParameterFromRequest(req, "b"));

            out.printf(iBeanRemote.solve(a, b, n));

        } catch (NumberFormatException e){
            out.print("1.12345");
        }
    }

    /**
     * @param req HttpServletRequest
     * @param nameOfParameter string
     * @return parameter
     */
    private String getParameterFromRequest(HttpServletRequest req,
                                           String nameOfParameter) {
        if (req.getParameter(nameOfParameter) != null){
            return req.getParameter(nameOfParameter);
        } else {
            return "1";
        }
    }

    /**
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException exception
     */
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws IOException {
        processRequest(req, resp);

    }

    /**
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException exception
     */
    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws IOException {
        processRequest(req, resp);
    }
}