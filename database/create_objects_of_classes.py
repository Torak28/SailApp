import database.db as db
from database.db import connection_to_db


def create_user(name, surname, email, password, number, role):
    user = db.User(first_name=name, last_name=surname, email=email,
                   password=password, phone_number=number, role_id=role)
    return user


def create_role(role_name):
    role = db.Role(role_name=role_name)
    return role


def create_gear(centre_id, name, quantity, price):
    gear = db.Gear(centre_id=centre_id, name=name, total_quantity=quantity, price_hour=price)
    return gear


def create_rental(user_id, gear_id, centre_id, start, end):
    rental = db.GearRental(user_id=user_id, gear_id=gear_id, centre_id=centre_id, rent_start=start, rent_end=end)
    return rental


def create_centre(name):
    centre = db.WaterCentre(name=name)
    return centre


def create_class(type_id, user_id, date):
    class_ = db.Class(class_type_id=type_id, user_id=user_id, class_date=date)
    return class_


def create_class_type(class_type):
    class_type = db.ClassType(class_type=class_type)
    return class_type


@connection_to_db
def add_object_to_database(obj, session=None):
    session.add(obj)


# role1 = create_role('User')
# role2 = create_role('Admin')
# user = create_user('Papa', 'Arab', 'jpnasto@gmail.com', 'chuj', '123123123', '2')

# add_object_to_database(role1)
# add_object_to_database(role2)
# add_object_to_database(user)
