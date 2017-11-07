package ctttest.common;

import canttouchthis.common.Authenticator;

import junit.framework.*;

public class TestAuthenticator extends TestCase {

    Authenticator auth;

    public void setUp() throws java.sql.SQLException {
        auth = new Authenticator("auth_db.sqlite");
    }

    public void testAdminIsValidUser() {

        final String ADMIN_USER = "admin";
        final String ADMIN_PWD = "nimda";

        try {
            assertTrue(auth.checkAuth(ADMIN_USER, ADMIN_PWD, Authenticator.UserType.USERTYPE_SERVER));
        }
        catch (java.sql.SQLException ex) {
            ex.printStackTrace();
            assertTrue("SQL error checking credentials!", false);
        }

    }

}
