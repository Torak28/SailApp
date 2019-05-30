from database.database_classes import connection_to_db, Role


@connection_to_db
def get_role_names_from_db(session=None):
    all_roles = session.query(Role).all()
    role_names = []
    for role in all_roles:
        role_names.append(role.role_name)
    return role_names


@connection_to_db
def get_role_name_by_id(role_id, session=None):
    return session.query(Role).filter_by(id=role_id).first().role_name