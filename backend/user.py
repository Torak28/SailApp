from database.database_classes import connection_to_db, User


@connection_to_db
def is_user_in_database_by_id(user_id, session=None):
    user_object = session.query(User).get(user_id)
    return True if user_object else False


@connection_to_db
def is_user_in_database_by_mail(email, session=None):
    user_object = session.query(User).filter_by(email=email).first()
    return True if user_object else False
