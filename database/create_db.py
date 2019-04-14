from sqlalchemy import create_engine
from sqlalchemy_utils import create_database, database_exists

engine = create_engine('postgres+psycopg2://postgres:12345@localhost:5432/db.py')
if not database_exists(engine.url):
    create_database(engine.url)

print(database_exists(engine.url))
