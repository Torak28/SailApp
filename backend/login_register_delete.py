import secrets
from passlib.hash import sha256_crypt as crypter
from backend.user import is_user_in_database_by_mail
from database.create_objects_of_classes import create_user, add_object_to_database
from database.database_classes import connection_to_db, Role, User


def hash_password(password):
    encrypter = (crypter.using(salt_size=0))
    hashed_password = (encrypter.hash(password)).split("$")[-1]
    return hashed_password


@connection_to_db
def get_role_id(role, session=None):
    return session.query(Role).filter_by(role_name=role).first().id


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


token = login_user('jan.kowalski@test.com', 'testowe_haslo')
print(token)
check_if_token_is_active(token)
print(get_user_data_by_token_value(token))
logout_user(token)
print(check_if_token_is_active(token))
print(get_user_data_by_token_value(token))
