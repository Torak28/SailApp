import database.database_classes as db
from database.database_classes import connection_to_db


def create_user(name, surname, email, password, number, role, is_accepted):
    is_accepted = 1 if is_accepted else 0
    print(email, is_accepted)
    user = db.User(first_name=name, last_name=surname, email=email,
                   password=password, phone_number=number, role_id=role, account_status=is_accepted)
    return user


def create_role(role_name):
    role = db.Role(role_name=role_name)
    return role


def create_gear(centre_id, name, quantity, price):
    gear = db.Gear(centre_id=centre_id, name=name, total_quantity=quantity, price_hour=price)
    return gear


def create_rental(user_id, gear_id, centre_id, start, end, amount, is_accepted):
    is_accepted = 1 if is_accepted else 0
    rental = db.GearRental(user_id=user_id, gear_id=gear_id, centre_id=centre_id,
                           rent_start=start, rent_end=end, rent_amount=amount, rent_status=is_accepted)
    return rental


def create_centre(owner_id, name, latitude, longitude, telephone_number):
    centre = db.WaterCentre(owner_id=owner_id, name=name, latitude=latitude, longitude=longitude,
                            contact_number=telephone_number)
    return centre


def create_class(type_id, user_id, date):
    class_ = db.Class(class_type_id=type_id, user_id=user_id, class_date=date)
    return class_


def create_class_type(class_type):
    class_type = db.ClassType(class_type=class_type)
    return class_type


def create_picture(filepath, centre_id):
    picture = db.Picture(file_path=filepath, water_centre_id=centre_id)
    return picture


@connection_to_db
def add_object_to_database(obj, session=None):
    print("Adding object: {}".format(obj))
    session.add(obj)
