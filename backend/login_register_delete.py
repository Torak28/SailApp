import secrets
from passlib.hash import sha256_crypt as crypter
from backend.user import is_user_in_database_by_mail
from database.create_objects_of_classes import create_user, add_object_to_database, create_gear, create_rental
from database.database_classes import connection_to_db, Role, User, Gear, GearRental, WaterCentre
from datetime import datetime                  # do overlap ten i nast import
from collections import namedtuple
Range = namedtuple('Range', ['start', 'end'])
from flask_jwt_extended import (
    JWTManager,
    create_access_token,
    create_refresh_token,
    jwt_required
)


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


#tutaj blad
@connection_to_db
def update_user(id, session=None):
    return session.update(User).where(id=id).value(first_name=first_name, last_name=last_name, email=email, phone_number=phone_number, password=hash_password(password))

#tu tez
@connection_to_db
def update_centre(centre_id, session=None):
    return session.update(WaterCentre).where(id=centre_id).value(name=name, latitude=latitude, longitude=longitude)


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


def register_new_person(first_name, last_name, email, password, phone_number, role):
    if role.lower() in ['user', 'admin', 'owner']:
        hashed_password = hash_password(password)
        role_id = get_role_id(role)
        if not is_user_in_database_by_mail(email):
            user = create_user(first_name, last_name, email, hashed_password, phone_number, role_id)
            add_object_to_database(user)
            print("Added new {} - {} {}".format(role, first_name, last_name))
            return True
        else:
            print("user already exists!")
            return False
    else:
        return False
    #    print ("Please provide proper name (User/Admin), not '{}'.".format(user_or_admin))

#tutaj moje nowe funkcje
#czy jest potrzebna??
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
    #else:
    #    "Please provide proper name (User/Admin), not '{}'.".format(user_or_admin)


def change_data(user_id, first_name, last_name, email, phone_number, password):
    fname=get_fname(user_id)  #od tego miejsca
    lname=get_lname(user_id)
    pnumer=get_pnumber(user_id)
    pword=get_password(user_id)
    fname=first_name
    lname=last_name
    pnumer=phone_number
    pword=hash_password(password) #do tego to  będzie niepotrzebne, jesli session.update dziala
    mail=get_email(user_id)
    if mail != email:
        if  is_user_in_database_by_mail(email):
            print("mail already exists!")
        else:
            mail=email
            print("data changed")
    print("ok")
    update_user()     #tutaj cos sie wysypuje, to session.update
    print("ok")
    #tutaj cos sie wysypuje, to session...


def change_data_rental(centre_id,name,latitude,longitude):
    nazwa=get_name(centre_id)#
    lat=get_lat(centre_id)
    long=get_long(centre_id)
    nazwa=name
    lat=latitude
    long=longitude# to wszystko niepotezebne jeśli upadte dziala dobrze, tak samo funkcje
    update_centre(centre_id)


def gear_type_add(centre_id, name, price, quantity=0):
    gear = create_gear(centre_id, name, quantity, price)
    add_object_to_database(gear)
    print("Added new {} - quantity:{}, price:{}".format(gear, quantity, price_hour))

@connection_to_db
def gear_type_delete(centre_id, name, session=None):
    session.query(Gear).filter_by(centre_id=centre_id).filter_by(name=name).delete()
    print("Deleted a gear type.")

@connection_to_db
def gear_add(centre_id, name, quantity):
    q=get_quantity(centre_id, name, session=None)
    q += quantity
    session.update(WaterCentre).where(id=centre_id).value(quantity=q)
    print("Current quantiry of {}: {}".format(name, quantity))

@connection_to_db
def gear_delete(centre_id, name, quantity, session=None):
    q=get_quantity(centre_id, name, session=None)
    q -= quantity
    session.update(WaterCentre).where(id=centre_id).value(quantity=q)
    print("Current quantiry of {}: {}".format(name, quantity))


#czy to jest ok?
@connection_to_db
def get_gear_owner(centre_id,gear_id, session=None):
    return session.query(Gear).filter_by(centre_id=centre_id).filter_by(id=gear_id).all()


@connection_to_db
def all_gear_owner(centre_id, session=None):
    return session.query(Gear).filter_by(centre_id=centre_id).all()


@connection_to_db
def get_gear_client(gear_id, session=None):
    return session.query(Gear).filter_by(id=gear_id).all()


@connection_to_db
def all_gear_client(session=None):
    return session.query(Gear).all()


#nast 3 funkcje do sprawdzenia, wzsystkie do rent gear prze usera
def is_overlapped(start1,end1, start2, end2, rent_amount, quantity_gear): #wszystkie - datetime; z indeksem 1 - od usera, z indeksem 2 - z bazy wszystkie
    r1 = Range(start=start1, end=end1)
    r2 = Range(start=start2, end=end2)
    x=0
    q=0
    while x<len(start2):
        #print(r2.start[x])
        #print(r2.end[x])
        latest_start = max(r1.start, r2.start[x])
        earliest_end = min(r1.end, r2.end[x])
        if(latest_start < earliest_end):   #jesli range podany przez usera sie naklada z jakimkolwiek gearRental z bazy
            q=q+rent_amount[x]              #zwiekszamy wartosc q, czyli ile sprzetu w range podanym przez usera jest zajete
            if (q+rent_amount)>=quantity_gear:             #jesli ilosc zajetego sprzetu + ilosc sprzetu podanego przez usera > ilosc sprzetu jakim dysponuje watercentre, to true
                return True
        x+=1
    return False


@connection_to_db
def check(start, end, centre_id, gear_id, session=None):
    rent_start_db=session.query(GearRental).filter_by(centre_id=centre_id).oredr_by('gear_rental_id_seq').all().rent_start  #czy to z order ok?
    rent_end_db=session.query(GearRental).filter_by(centre_id=centre_id).order_by('gear_rental_id_seq').all().rent_end
    rent_amount_db=session.query(GearRental).filter_by(centre_id=centre_id).order_by('gear_rental_id_seq').all().rent_amount
    quantity_gear_watercentre=session.query(Gear).filter_by(centre_id=centre_id).filter_by(gear_id).first().total_quantity
    if not is_overlapped(start,end,rent_start_db,rent_end_db,rent_amount_db,quantity_gear_watercentre):    #jesli is_overlapped jest true. to nie mozna wypozyczyc, bo za malo sprzetu zostalo
        return True
    else:
        return False


def gear_rent(user_id, gear_id, centre_id, start, end, rent_amount):
    c=check(start,end,centre_id,gear_id)
    if c:
        rental = create_rental(user_id, gear_id, centre_id, start, end,rent_amount)
        add_object_to_database(rental)
        print("created rental")
    else:
        print("data not available")


@connection_to_db
def get_all_centres(session=None):
    return session.query(WaterCentre).all()  #chcemy tutaj id czy wszystko o watercentre?


@connection_to_db
def get_all_centre_gear(centre_id, session=None):
    return session.query(Gear).filter_by(centre_id=centre_id).all()


@connection_to_db
def get_all_user_rentals(user_id, session=None):
    return session.query(GearRental).filter_by(user_id=user_id).all()





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
        access_token = create_access_token(identity=user_object.id, fresh=True)
        refresh_token = create_refresh_token(user_object.id)
        print("Logged in - token added.")
        return access_token, refresh_token
    else:
        print("Log in not successfull.")
        return None, None


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
