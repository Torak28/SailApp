import secrets
from passlib.hash import sha256_crypt as crypter
import backend.user as user_
from database.create_objects_of_classes import create_user, add_object_to_database, create_gear, create_rental
from database.database_classes import connection_to_db, Role, User, Gear, GearRental, WaterCentre
from datetime import datetime                  # do overlap ten i nast import
import backend.roles as roles
from flask_jwt_extended import create_access_token, create_refresh_token
# Range = namedtuple('Range', ['start', 'end'])


def hash_password(password):
    encrypter = (crypter.using(salt_size=0))
    hashed_password = (encrypter.hash(password)).split("$")[-1]
    return hashed_password


@connection_to_db
def change_password(kwargs, session=None):
    kwargs['password'] = hash_password(kwargs['password'])
    session.query(User).filter_by(id=kwargs['id']).update(kwargs)


@connection_to_db
def get_role_id(role, session=None):
    return session.query(Role).filter_by(role_name=role).first().id


def register_new_person(first_name, last_name, email, password, phone_number, role):
    if role.lower() in ['user', 'admin', 'owner']:
        hashed_password = hash_password(password)
        role_id = get_role_id(role.lower().capitalize())
        if not user_.is_user_in_database_by_mail(email):
            is_accepted = True if role.lower() in ['user', 'admin'] else False
            user = create_user(first_name, last_name, email, hashed_password, phone_number, role_id, is_accepted)
            add_object_to_database(user)
            print("Added new {} - {} {}".format(role, first_name, last_name))
            return True
        else:
            print("user already exists!")
            return False
    else:
        return False


# @connection_to_db
# def check(start, end, centre_id, gear_id, session=None):
#     rent_start_db=session.query(GearRental).filter_by(centre_id=centre_id).oredr_by('gear_rental_id_seq').all().rent_start  #czy to z order ok?
#     rent_end_db=session.query(GearRental).filter_by(centre_id=centre_id).order_by('gear_rental_id_seq').all().rent_end
#     rent_amount_db=session.query(GearRental).filter_by(centre_id=centre_id).order_by('gear_rental_id_seq').all().rent_amount
#     quantity_gear_watercentre=session.query(Gear).filter_by(centre_id=centre_id).filter_by(gear_id).first().total_quantity
#     if not is_overlapped(start,end,rent_start_db,rent_end_db,rent_amount_db,quantity_gear_watercentre):    #jesli is_overlapped jest true. to nie mozna wypozyczyc, bo za malo sprzetu zostalo
#         return True
#     else:
#         return False


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
        role_name = roles.get_role_name_by_id(user_object.role_id)
        account_status = user_.get_user_status(user_object.id)
        print(account_status)

        if str(account_status) == '-1':
            account_status = 'denied'
        elif str(account_status) == '0':
            account_status = 'pending'
        elif str(account_status) == '1':
            account_status = 'accepted'
        return access_token, refresh_token, role_name, account_status
    else:
        return None, None, None, None


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
