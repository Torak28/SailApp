from sqlalchemy import create_engine
from sqlalchemy_utils import create_database, database_exists
import os

engine = create_engine(os.environ['DATABASE_URL'])
if not database_exists(engine.url):
    create_database(engine.url)

print(database_exists(engine.url))
