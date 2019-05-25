from database.create_objects_of_classes import create_gear, add_object_to_database
from database.database_classes import connection_to_db, Gear


def add_gear(centre_id, gear_name, gear_price, gear_quantity):
    gear = create_gear(centre_id, gear_name, gear_quantity, gear_price)
    add_object_to_database(gear)


@connection_to_db
def update_gear(centre_id, gear_id, gear_name, gear_price, gear_quantity, session=None):
    session.query(Gear).filter_by(centre_id=centre_id, id=gear_id).update({'price_hour': gear_price,
                                                                           'total_quantity': gear_quantity,
                                                                           'name': gear_name})


@connection_to_db
def delete_gear(centre_id, gear_id, session=None):
    session.query(Gear).filter_by(id=gear_id, centre_id=centre_id).delete()


@connection_to_db
def get_all_gear(centre_id, session=None):
    all_gear = session.query(Gear).filter_by(centre_id=centre_id).all()
    list_of_all_gears = []
    for gear in all_gear:
        one_gear = {'name': gear.name,
                    'gear_price': gear.price_hour,
                    'total_quantity': gear.total_quantity,
                    'id': gear.id}
        list_of_all_gears.append(one_gear)
    return {'all_gear': list_of_all_gears}
