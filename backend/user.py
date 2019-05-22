from database.database_classes import connection_to_db, User, Role, WaterCentre


@connection_to_db
def is_user_in_database_by_id(user_id, session=None):
    user_object = session.query(User).get(user_id)
    return True if user_object else False


@connection_to_db
def is_user_in_database_by_mail(email, session=None):
    user_object = session.query(User).filter_by(email=email).first()
    return True if user_object else False


@connection_to_db
def is_user_the_owner(user_id, session=None):
    user_object = session.query(User, Role).filter(User.role_id == Role.id, User.id == user_id).first()
    role = user_object.Role.role_name
    return role.lower() == 'owner'


@connection_to_db
def is_owner_the_centre_owner(owner_id, centre_id, session=None):
    water_centre = session.query(WaterCentre).filter(WaterCentre.owner_id == owner_id,
                                                     WaterCentre.id == centre_id).first()
    return True if water_centre else False


@connection_to_db
def get_user_by_id(user_id, session=None):
    user_object = session.query(User).filter_by(id=user_id).first()
    session.expunge(user_object)
    return user_object


@connection_to_db
def update_user(kwargs, session=None):
    session.query(User).filter_by(id=kwargs['id']).update(kwargs)
    return True
