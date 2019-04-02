To make database work you need a few things:

-pgadmin 4

-postgre(can be newest, doesnt really matter)

-python3+ with sqlalchemy, psycopg2


After you install this stuff just open pgadmin, create a new user preferably with password and create the database. Then open the db.py file and modify the DATABASE_URI so it matches with your just created database and user.
Now just run the db.py with uncommented 'recreate_database' method, refresh the view in pgadmin and check if the tables have been added. If they did - great, if they didnt - probably you have to modify the postgre config file (google).

In case you don't want to create the database manually:
Download the sqlalchemy-utils (pip install sqlalchemy-utils) and specify the name of the database in the create_engine argument, if the script returns True - you are good to go.
