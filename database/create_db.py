from sqlalchemy import create_engine
from sqlalchemy_utils import create_database, database_exists
from database.DATABASE_URI import DATABASE_URI

engine = create_engine(DATABASE_URI)
if not database_exists(engine.url):
    create_database(engine.url)
print(database_exists(engine.url))
