"""
generate_auth_db.py
--------------------

Creates a test database for username/password hash pairs.
In production, this file would likely contain more accounts and be stored on a remote source.

Server test credentials: admin/nimda
Client test credentials: user/resu
"""

import sqlite3
import hashlib

TEST_CREDENTIALS = [
    ["admin", "nimda"],
    ["user", "resu"]
]


def main():
    # create new sqlite database
    con = sqlite3.connect("auth_db.sqlite")
    cur = con.cursor()

    # create table for user data
    cur.execute("""
        CREATE TABLE data_users 
            (uname STRING NOT NULL, pwdHash STRING NOT NULL);
    """)

    for uname, pwd in TEST_CREDENTIALS:
        pwd_digest = hashlib.sha256(pwd).hexdigest()
        print "Adding row %s, %s" % (uname, pwd_digest)

        cur.execute("INSERT INTO data_users VALUES (?, ?);", (uname, pwd_digest))

    con.commit()
    con.close()


if __name__ == "__main__":
    main()
