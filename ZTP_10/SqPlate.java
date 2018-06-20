import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MM
 * @version 1.0
 */
@Path("/plate")
public class SqPlate {

    private Connection connection;
    private List<PlateCost> plateCosts;
    private IMyPlate plate;

    /**
     * @param dataSource data source param
     * @param table table name param
     * @return text with cost of cuts
     */
    @GET
    @Path("/{datasource}/{table}")
    public String getCostOfCutPlate(
            @PathParam("datasource") String dataSource,
            @PathParam("table") String table
    ) {

        try {
            getDatabaseConnection(dataSource);
            getDataFromTable(table);
            return getResult().toString();
        } catch (NamingException e) {
            return "1.0";
        } catch (SQLException e) {
            return "2.0";
        }
    }

    /**
     * @return cost of cuts
     */
    private Double getResult() {
        plate = new MyPlate();
        return plate.calculateCostOfCuts(plateCosts);
    }

    /**
     * @param table table name
     * @throws SQLException when problem with query
     */
    private void getDataFromTable(String table) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM " + table;

        storeData(statement.executeQuery(query));
    }

    /**
     * @param queryResult result of execute query
     * @throws SQLException when problem occur
     */
    private void storeData(ResultSet queryResult) throws SQLException {
        if (plateCosts == null) {
            plateCosts = new ArrayList<>();
            while (queryResult.next()) {
                Long id = queryResult.getLong("id");
                Double x = queryResult.getDouble("x");
                Double y = queryResult.getDouble("y");
                plateCosts.add(new PlateCost(id, x, y));
            }
        }
    }

    /**
     * @param dataSourceString DataSource name
     * @throws NamingException when problem with context
     * @throws SQLException when problem with connection
     */
    private void getDatabaseConnection(String dataSourceString)
            throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource dataSource =
                (DataSource) context.lookup(
                        "jdbc/" + dataSourceString
                );
        connection = dataSource.getConnection();
    }


}
