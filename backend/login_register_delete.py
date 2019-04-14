import database.db as db
from database.insert import *
from backend.user import *
from passlib.hash import sha256_crypt as crypter
import secrets


def hash_password(password):
    encrypter = (crypter.using(salt_size=0))
    hashed_password = (encrypter.hash(password)).split("$")[-1]
    return hashed_password


def get_role_id(role):
    return db.s.query(db.Role).filter_by(role_name=role).first().id


def register_new_person(first_name, last_name, email, password, phone_number, user_or_admin='User'):
    if user_or_admin in ['User', 'Admin']:
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


def delete_user(user_id):
    if db.s.query(db.User).get(user_id):
        db.s.query(db.User).filter_by(id=user_id).delete()
        db.s.commit()
        print("Deleted an account.")
    else:
        print("Couldn't delete nonexistent row.")


def delete_user_by_email(email):
    if db.s.query(db.User).get(email):
        db.s.query(db.User).filter_by(email=email).delete()
        db.s.commit()
        print("Deleted an account.")
    else:
        print("Couldn't delete nonexistent row.")


def login_user(email, password):
    hashed_password = hash_password(password)
    user_object = db.s.query(db.User).filter_by(email=email, password=hashed_password).first()
    if user_object:
        token = secrets.token_hex(8)  # has to be >32
        user_object.auth_token = token
        db.s.commit()
        print("Logged in - token added.")
        return token
    else:
        print("Log in not successfull.")


def logout_user(token):
    user_object = db.s.query(db.User).filter_by(auth_token=token).first()
    if user_object:
        user_object.auth_token = None
        db.s.commit()
        print(user_object.email)
        print("User ({}) logged out - token deleted.".format(user_object.email))


def check_if_token_is_active(token):
    user_object = db.s.query(db.User).filter_by(auth_token=token).first()
    return True if user_object else False


def get_user_data_by_token_value(token):
    user_object = db.s.query(db.User).filter_by(auth_token=token).first()
    if user_object:
        user_data = dict()
        user_data['name'] = user_object.first_name
        user_data['surname'] = user_object.last_name
        user_data['email'] = user_object.email
        user_data['phone_number'] = user_object.phone_number
        return user_data
    return None


token = login_user('jan.kowalski@test.com', 'testowe_haslo')
print(token)
check_if_token_is_active(token)
print(get_user_data_by_token_value(token))
logout_user(token)
print(check_if_token_is_active(token))
print(get_user_data_by_token_value(token))
