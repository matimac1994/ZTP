import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * @author MM
 * @version 1.0
 */

public class Control extends HttpServlet {

    static final long serialVersionUID = 123L;

    @EJB
    private IControlRemote iControlRemote;

    /**
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws ServletException when problem with servlet
     * @throws IOException when problem with IO operations
     */
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.register(req, resp);
        } catch (Exception e){
            resp.getWriter().print(7);
        }
    }

    /**
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws ServletException when problem with servlet
     * @throws IOException when problem with IO operations
     */
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    /**
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException when printWriter not found
     */
    private void register(HttpServletRequest req,
                          HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        Enumeration<String> paramNames = req.getParameterNames();
        if (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            this.processRequest(paramName, req.getParameter(paramName), out);
        }

    }

    /**
     * @param paramName param name
     * @param parameter parameter
     * @param out print writer
     */
    private void processRequest(String paramName,
                                String parameter,
                                PrintWriter out) {
        switch(paramName) {
            case "login":
                this.iControlRemote.start();
                break;
            case "logout":
                this.iControlRemote.stop();
                break;
            case "result":
                out.print(this.iControlRemote.counter() - this.iControlRemote.errors());
                break;
            case "state":
                this.handleStateParameter(parameter);
                break;
            default:
                break;
        }
    }

    /**
     * @param parameter value of parameter
     */
    private void handleStateParameter(String parameter) {
        if (parameter != null && !parameter.isEmpty()) {
            this.iControlRemote.increment(Integer.parseInt(parameter));
        } else {
            this.iControlRemote.increment(1);
        }

    }
}
