package canttouchthis.common;

import java.util.Base64;
import java.sql.*;

/**
 * Checks whether or not a username/password pair is valid.
 */
public class Authenticator {

    public enum UserType {
        USERTYPE_CLIENT,
        USERTYPE_SERVER
    }

    private Connection _conn;

    public Authenticator(String dbpath) throws SQLException {
        String url = "jdbc:sqlite:" + dbpath;
        _conn = DriverManager.getConnection(url);
    }

    public boolean checkAuth(String username, String password, UserType type) throws SQLException {

        String typeString = type==UserType.USERTYPE_SERVER ? "SERVER" : "CLIENT";
        String query = "SELECT pwdHash FROM data_users WHERE uname=? AND TYPE=?";

        PreparedStatement stmt = _conn.prepareStatement(query,
                                                        ResultSet.TYPE_FORWARD_ONLY,
                                                        ResultSet.CONCUR_READ_ONLY);
        stmt.setString(1, username);
        stmt.setString(2, typeString);

        ResultSet s = stmt.executeQuery();
        if (s.next()) {

            // SHA-256 hash of password should match value in database.
            String reqHash = Base64.getEncoder().encodeToString(IntegrityChecking.generateMessageDigest(password));
            String dbHash = s.getString(1);
            return reqHash.equals(dbHash);

        }
        else {
            // no rows in result set. User does not exist.
            return false;
        }

    }

}
