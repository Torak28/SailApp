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
def is_user_the_admin(user_id, session=None):
    user_object = session.query(User, Role).filter(User.role_id == Role.id, User.id == user_id).first()
    role = user_object.Role.role_name
    return role.lower() == 'admin'


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


@connection_to_db
def get_user_status(user_id, session=None):
    return session.query(User).filter_by(id=user_id).first().account_status


@connection_to_db
def decide_about_owner(owner_id, decision, session=None):
    owner = session.query(User).filter_by(id=owner_id).first()
    owner.account_status = decision
    session.commit()


@connection_to_db
def get_pending_owners(session=None):
    owners = session.query(User).filter_by(account_status=0).all()
    list_of_formatted_pending_owners = []
    for owner in owners:
        ready_owner = dict()
        ready_owner['first_name'] = owner.first_name
        ready_owner['last_name'] = owner.last_name
        ready_owner['email'] = owner.email
        ready_owner['phone_number'] = owner.phone_number
        ready_owner['owner_id'] = owner.id
        list_of_formatted_pending_owners.append(ready_owner)
    return list_of_formatted_pending_owners