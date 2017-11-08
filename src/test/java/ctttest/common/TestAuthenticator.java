package ctttest.common;

import canttouchthis.common.auth.Authenticator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import canttouchthis.common.auth.Identity;
import junit.framework.*;

public class TestAuthenticator extends TestCase {

    public void testAdminIsValidUser() {

        final String ADMIN_USER = "admin";
        final String ADMIN_PWD = "nimda";

        try {
            Authenticator auth = new Authenticator("auth_db.sqlite");
            assertTrue(auth.checkAuth(ADMIN_USER, ADMIN_PWD, Authenticator.UserType.USERTYPE_SERVER) != null);
        }
        catch (java.sql.SQLException ex) {
            ex.printStackTrace();
            assertTrue("SQL error checking credentials!", false);
        }

    }

    public void testAuthDBIsReadOnly() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:auth_db.sqlite");
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO data_users VALUES (\"I\", \"HACKED\", \"YOU\", \"LOSER\");");
            stmt.execute();
        }
        catch (java.sql.SQLException ex) {
            assertTrue("Auth DB is not read only!", ex.getMessage().contains("[SQLITE_READONLY]"));
            return;
        }
        assertTrue("Auth DB is not read only!", false);
    }

    public void testValidIdentityIsValidated() {
        try {
            // SETUP
            Authenticator auth = new Authenticator("auth_db.sqlite");
            Identity ident = auth.checkAuth("admin", "nimda",
                                            Authenticator.UserType.USERTYPE_SERVER);

            // EXEC + VERIFY
            assertTrue("Valid identity was not validated!", auth.verifyIdentity(ident));
        }
        catch (java.sql.SQLException ex) {
            ex.printStackTrace();
            assertTrue("Error reading auth db!", false);
        }
    }

    public void testInvalidIdentityIsInvalidated() {
        try {
            // SETUP
            Authenticator auth = new Authenticator("auth_db.sqlite");
            Identity ident = Identity.generateBadIdent();

            // EXEC + VERIFY
            assertFalse("Invalid identity was not invalidated!", auth.verifyIdentity(ident));
        }
        catch (java.sql.SQLException ex) {
            ex.printStackTrace();
            assertTrue("Error reading auth db!", false);
        }
    }
}


