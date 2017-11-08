"""
generate_auth_db.py
--------------------

Creates a test database for username/password hash pairs.
In production, this file would likely contain more accounts and be stored on a remote source.

Server test credentials: admin/nimda
Client test credentials: user/resu

Database schema:
    uname STRING NOT NULL: Unique user ID
    pwdHash STRING NOT NULL: SHA-256 hash of user's password (base64)
    type STRING NOT NULL: Scope of user (either CLIENT or SERVER)
    nonce STRING NOT NULL: 256-bit random number (base64)
"""

import base64
import os
import stat
import sqlite3
import hashlib

AUTH_DB_NAME = "auth_db.sqlite"

TEST_CREDENTIALS = [
    ["admin", "nimda", "SERVER"],
    ["user", "resu", "CLIENT"]
]


def main():
    # remove an old auth db if present
    if os.path.exists(AUTH_DB_NAME):
        os.remove(AUTH_DB_NAME)

    # create new sqlite database
    con = sqlite3.connect(AUTH_DB_NAME)
    cur = con.cursor()

    # create table for user data
    cur.execute("""
        CREATE TABLE data_users 
            (uname STRING NOT NULL, pwdHash STRING NOT NULL, type STRING NOT NULL, nonce STRING NOT NULL);
    """)

    for uname, pwd, type in TEST_CREDENTIALS:
        pwd_digest = base64.b64encode(hashlib.sha256(str.encode(pwd)).digest())
        print("Adding row %s, %s" % (uname, pwd_digest))

        # generate 256-bit random nonce for each user
        nonce = base64.b64encode(os.urandom(32))

        cur.execute("INSERT INTO data_users VALUES (?, ?, ?, ?);", (uname, pwd_digest, type, nonce))

    con.commit()
    con.close()

    # make sqlite file read-only
    os.chmod(AUTH_DB_NAME, stat.S_IREAD)


if __name__ == "__main__":
    main()
