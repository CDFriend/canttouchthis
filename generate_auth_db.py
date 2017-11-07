"""
generate_auth_db.py
--------------------

Creates a test database for username/password hash pairs.
In production, this file would likely contain more accounts and be stored on a remote source.

Server test credentials: admin/nimda
Client test credentials: user/resu
"""

import base64
import os
import sqlite3
import hashlib

TEST_CREDENTIALS = [
    ["admin", "nimda", "SERVER"],
    ["user", "resu", "CLIENT"]
]


def main():
    # remove an old auth db if present
    if os.path.exists("auth_db.sqlite"):
        os.remove("auth_db.sqlite")

    # create new sqlite database
    con = sqlite3.connect("auth_db.sqlite")
    cur = con.cursor()

    # create table for user data
    cur.execute("""
        CREATE TABLE data_users 
            (uname STRING NOT NULL, pwdHash STRING NOT NULL, type STRING NOT NULL);
    """)

    for uname, pwd, type in TEST_CREDENTIALS:
        pwd_digest = base64.b64encode(hashlib.sha256(str.encode(pwd)).digest())
        print("Adding row %s, %s" % (uname, pwd_digest))

        cur.execute("INSERT INTO data_users VALUES (?, ?, ?);", (uname, pwd_digest, type))

    con.commit()
    con.close()


if __name__ == "__main__":
    main()
