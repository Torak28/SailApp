from sqlalchemy import create_engine
from sqlalchemy_utils import create_database, database_exists
import os
try:
    DATABASE_URI = os.environ['DATABASE_URL']
except KeyError:
    DATABASE_URI = 'postgres+psycopg2://postgres:12345@localhost:5432/sailappdb'
finally:
    engine = create_engine(DATABASE_URI)
    if not database_exists(engine.url):
        create_database(engine.url)
    print(database_exists(engine.url))
