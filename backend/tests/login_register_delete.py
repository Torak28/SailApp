import secrets
from passlib.hash import sha256_crypt as crypter
from backend.user import is_user_in_database_by_mail
from database.create_objects_of_classes import create_user, add_object_to_database, create_gear, create_rental
from database.database_classes import connection_to_db, Role, User, Gear, GearRental, WaterCentre
from datetime import datetime                  #do overlap ten i nast import
from collections import namedtuple
Range = namedtuple('Range', ['start', 'end'])


def hash_password(password):
    encrypter = (crypter.using(salt_size=0))
    hashed_password = (encrypter.hash(password)).split("$")[-1]
    return hashed_password


@connection_to_db
def get_role_id(role, session=None):
    return session.query(Role).filter_by(role_name=role).first().id

#pobranie dancyh usera z bazy
@connection_to_db
def get_fname(id, session=None):
    return session.query(User).filter_by(id=id).first().first_name

@connection_to_db
def get_lname(id, session=None):
    return session.query(User).filter_by(id=id).first().last_name

@connection_to_db
def get_email(id, session=None):
    return session.query(User).filter_by(id=id).first().email

@connection_to_db
def get_pnumber(id, session=None):
    return session.query(User).filter_by(id=id).first().phone_number

@connection_to_db
def get_password(id, session=None):
    return session.query(User).filter_by(id=id).first().password

#get dla gear
@connection_to_db
def get_quantity(centre_id, name, session=None):
    return session.query(Gear).filter_by(centre_id=centre_id).filter_by(name=name).first().quantity

#get dla WaterCentre
@connection_to_db
def get_name(centre_id, session=None):
    return session.query(WaterCentre).filter_by(centre_id=centre_id).first().name

@connection_to_db
def get_lat(centre_id, session=None):
    return session.query(WaterCentre).filter_by(centre_id=centre_id).first().latitude

@connection_to_db
def get_long(centre_id, session=None):
    return session.query(WaterCentre).filter_by(centre_id=centre_id).first().longitude


@connection_to_db
def register_new_person(first_name, last_name, email, password, phone_number, user_or_admin='User'):
    if user_or_admin in ['User', 'Admin', 'Owner']:  #dodalem Owner, bo mamy teraz 3 role
        hashed_password = hash_password(password)
        role_id = get_role_id(user_or_admin)
        if not is_user_in_database_by_mail(email):
            user = create_user(first_name, last_name, email, hashed_password, phone_number, role_id)
            add_object_to_database(user)
            print("Added new {} - {} {}".format(user_or_admin, first_name, last_name))
        else:
            print("user already exists!")
    else:
        "Please provide proper name (User/Admin), not '{}'.".format(user_or_admin)

#tutaj moje nowe funkcje
#czy jest potrzebna??
@connection_to_db
def register_new_owner(first_name, last_name, email, password, phone_number, user_or_admin='Owner'):
    if user_or_admin in ['User', 'Admin', 'Owner']:
        hashed_password = hash_password(password)
        role_id = get_role_id(user_or_admin)
        if not is_user_in_database_by_mail(email):
            user = create_user(first_name, last_name, email, hashed_password, phone_number, role_id)
            add_object_to_database(user)
            print("Added new {} - {} {}".format(user_or_admin, first_name, last_name))
        else:
            print("user already exists!")
    else:
        "Please provide proper name (User/Admin), not '{}'.".format(user_or_admin)


@connection_to_db
def change_data(id, first_name, last_name, email, phone_number, password):
    fname=get_fname(id)
    lname=get_lname(id)
    mail=get_email(id)
    pnumer=get_pnumber(id)
    pword=get_password(id)
    fname=first_name
    lname=last_name
    pnumer=phone_number
    pword=hash_password(password)
    if mail != email:
        if email == session.query(User).filter_by(email=email).first():
            print("mail already exists!")
        else:
            mail=email


@connection_to_db
def change_data_rental(centre_id,name,latitude,longitude):
    nazwa=get_name(centre_id)
    lat=get_lat(centre_id)
    long=get_long(centre_id)
    nazwa=name
    lat=latitude
    long=longitude


@connection_to_db
def gear_type_add(centre_id, name, price, quantity=0):
    gear = create_gear(centre_id, name, quantity, price)
    add_object_to_database(gear)
    print("Added new {} - quantity:{}, price:{}".format(gear, quantity, price_hour))


@connection_to_db
def gear_type_delete(centre_id, name):
    session.query(Gear).filter_by(centre_id=centre_id).filter_by(name=name).delete()
    print("Deleted a gear type.")


@connection_to_db
def gear_add(centre_id, name, quantity):
    q=get_quantity(centre_id, name)
    q += quantity
    print("Current quantiry of {}: {}".format(name, quantity))


@connection_to_db
def gear_delete(centre_id, name, quantity):
    q=get_quantity(centre_id, name)
    q -= quantity
    print("Current quantiry of {}: {}".format(name, quantity))


#czy to jest ok?
@connection_to_db
def get_gear_owner(centre_id,id):
    a=session.query(Gear).filter_by(centre_id=centre_id).filter_by(id=id).all()
    return a


@connection_to_db
def all_gear_owner(centre_id):
    a=session.query(Gear).filter_by(centre_id=centre_id).all()
    return a


@connection_to_db
def get_gear_client(gear_id):
    a=session.query(Gear).filter_by(id=gear_id).all()
    return a


@connection_to_db
def all_gear_client():
    a=session.query(Gear).all()
    return a


def is_overlapped(start1,end1, start2, end2): #wszystkie - datetime; z indeksem 1 - od usera, z indeksem 2 - z bazy wszystkie
    r1 = Range(start=start1, end=end1)
    r2 = Range(start=start2, end=end2)
    x=0
    while x<len(start2):
        print(r2.start[x])
        print(r2.end[x])
        latest_start = max(r1.start, r2.start[x])
        earliest_end = min(r1.end, r2.end[x])
        if(latest_start < earliest_end):
            return True
        x+=1
    return False


@connection_to_db
def check (start, end, centre_id):
    a=session.query(GearRental).filter_by(centre_id=centre_id).oredr_by('gear_rental_id_seq').all().rent_start  #czy to z order ok?
    b=session.query(GearRental).filter_by(centre_id=centre_id).order_by('gear_rental_id_seq').all().rent_end
    if not is_overlapped(start,end,a,b):    #jesli is_overlapped jest true. to sie nakladaja
        return True
    else:
        return False


@connection_to_db
def gear_rent(id, gear_id, centre_id, start, end,rent_amount):
    c=check(start,end,centre_id)
    if c:
    rental = create_rental(id, gear_id, centre_id, start, end,rent_amount)
    add_object_to_database(rental)


#dalej bez zmian
@connection_to_db
def delete_user(user_id, session=None):
    if session.query(User).get(user_id):
        session.query(User).filter_by(id=user_id).delete()
        print("Deleted an account.")
    else:
        print("Couldn't delete nonexistent row.")


@connection_to_db
def delete_user_by_email(email, session=None):
    if session.query(User).get(email):
        session.query(User).filter_by(email=email).delete()
        print("Deleted an account.")
    else:
        print("Couldn't delete nonexistent row.")


@connection_to_db
def login_user(email, password, session=None):
    hashed_password = hash_password(password)
    user_object = session.query(User).filter_by(email=email, password=hashed_password).first()
    if user_object:
        token = secrets.token_hex(8)  # TODO: has to be >32, now its easier to verify in DB
        user_object.auth_token = token
        print("Logged in - token added.")
        return token
    else:
        print("Log in not successfull.")


@connection_to_db
def logout_user(token, session=None):
    user_object = session.query(User).filter_by(auth_token=token).first()
    if user_object:
        user_object.auth_token = None
        print("User ({}) logged out - token deleted.".format(user_object.email))
    else:
        print("User could not be logged out - nobody's token!")


@connection_to_db
def check_if_token_is_active(token, session=None):
    user_object = session.query(User).filter_by(auth_token=token).first()
    return True if user_object else False


@connection_to_db
def get_user_data_by_token_value(token, session=None):
    user_object = session.query(User).filter_by(auth_token=token).first()
    if user_object:
        user_data = dict()
        user_data['name'] = user_object.first_name
        user_data['surname'] = user_object.last_name
        user_data['email'] = user_object.email
        user_data['phone_number'] = user_object.phone_number
        return user_data
    return None
