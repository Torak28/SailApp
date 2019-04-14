from database.insert import *


def is_user_in_database_by_id(user_id):
    user_object = db.s.query(db.User).get(user_id)
    return True if user_object else False


def is_user_in_database_by_mail(email):
    user_object = db.s.query(db.User).filter_by(email=email).first()
    return True if user_object else False


# print(is_user_id_in_database(1))
# print(is_user_in_database_by_mail('jpnasto@gmail.com'))