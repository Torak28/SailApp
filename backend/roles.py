from database.database_classes import connection_to_db, Role


@connection_to_db
def get_role_names_from_db(session=None):
    all_roles = session.query(Role).all()
    role_names = []
    for role in all_roles:
        role_names.append(role.role_name)
    return role_names
