import java.sql.*;

public class JDBC {

    private String url;
    private String user;
    private String pw;

    private Connection connection;

    //creates simple JDBC connection
    public JDBC(String url, String user, String pw) throws SQLException {
        this.url = url;
        this.user = user;
        this.pw = pw;

        connection = DriverManager.getConnection(this.url, this.user, this.pw);
    }

    //used to send query to database and get result set
    public ResultSet submitQuery(String query) throws  SQLException
    {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query);
        return result;
    }

    //used to update the database date (insert, delete)
    public void update(String query) throws SQLException
    {
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }
}
