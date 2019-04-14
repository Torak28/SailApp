from database.create_objects_of_classes import *
from database.db import connection_to_db

@connection_to_db
def is_user_in_database_by_id(user_id, session=None):
    user_object = session.query(db.User).get(user_id)
    return True if user_object else False


@connection_to_db
def is_user_in_database_by_mail(email, session=None):
    user_object = session.query(db.User).filter_by(email=email).first()
    return True if user_object else False
