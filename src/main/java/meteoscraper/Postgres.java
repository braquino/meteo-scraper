package meteoscraper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Postgres {

    private static final String hostEnvVar = "PG_HOST";
    private static final String userEnvVar = "PG_USER";
    private static final String passwordEnvVar = "PG_PWD";
    private String connStr;
    private String user;
    private String password;
    private Connection conn = null;

    private Postgres(String _connStr, String _user, String _password)
    {
        this.connStr = _connStr;
        this.user = _user;
        this.password = _password;
    }

    public static Postgres fromEnvVars() throws ExceptionInInitializerError
    {
        var host = System.getenv(hostEnvVar);
        if (host == null)
            throw new ExceptionInInitializerError("Missing required env var: " + hostEnvVar);
        var _user = System.getenv(userEnvVar);
        if (_user == null)
            throw new ExceptionInInitializerError("Missing required env var: " + userEnvVar);
        var _password = System.getenv(passwordEnvVar);
        if (_password == null)
            throw new ExceptionInInitializerError("Missing required env var: " + passwordEnvVar);
        var _connStr = String.format("jdbc:postgresql://%s:5432/postgres", host);
        return new Postgres(_connStr, _user, _password);
    }

    public void connect() throws SQLException
    {
        conn = DriverManager.getConnection(connStr, user, password);
    }

    public int executeQuery(String query) throws SQLException, RuntimeException
    {
        if (conn == null)
            throw new RuntimeException("'connect' must be called first");
        Statement st = conn.createStatement();
        int rowCount = st.executeUpdate(query);
        st.close();
        return rowCount;
    }

}
