from database.create_objects_of_classes import create_gear, add_object_to_database
from database.database_classes import connection_to_db, Gear, GearRental, WaterCentre
import datetime


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
        one_gear = {'gear_name': gear.name,
                    'gear_price': gear.price_hour,
                    'gear_quantity': gear.total_quantity,
                    'gear_id': gear.id}
        list_of_all_gears.append(one_gear)
    return list_of_all_gears


def get_currently_rented_gear_by_user(user_id):
    rented_gears = get_rented_gear_by_user(user_id)
    currently_rented_gear = []
    now = datetime.datetime.now()
    for one_gear in rented_gears:
        if one_gear['rent_start'] < now < one_gear['rent_end']:
            currently_rented_gear.append(one_gear)
    return currently_rented_gear


@connection_to_db
def get_rented_gear_by_user(user_id, session=None):
    rented_gears = session.query(GearRental, WaterCentre, Gear).filter(GearRental.user_id == user_id,
                                                                       WaterCentre.id == GearRental.centre_id,
                                                                       Gear.id == GearRental.gear_id).all()
    formatted_rented_gears = []
    for one_gear in rented_gears:
        one_gear_dict = dict()
        one_gear_dict['centre_id'] = one_gear.WaterCentre.id
        one_gear_dict['centre_name'] = one_gear.WaterCentre.name
        one_gear_dict['rent_id'] = one_gear.GearRental.id
        one_gear_dict['rent_start'] = one_gear.GearRental.rent_start
        one_gear_dict['rent_end'] = one_gear.GearRental.rent_end
        one_gear_dict['rent_quantity'] = one_gear.GearRental.rent_amount
        one_gear_dict['gear_name'] = one_gear.Gear.name
        one_gear_dict['centre_latitude'] = one_gear.WaterCentre.latitude
        one_gear_dict['centre_longitude'] = one_gear.WaterCentre.longitude
        one_gear_dict['centre_phone_number'] = one_gear.WaterCentre.contact_number

        formatted_rented_gears.append(one_gear_dict)
    return formatted_rented_gears


@connection_to_db
def get_rented_gear_by_centre(centre_id, session=None):  # TODO
    session.query(GearRental, WaterCentre, Gear).filter(WaterCentre.id == centre_id,
                                                        Gear.id == GearRental.gear_id).all()


@connection_to_db
def get_total_quantity(centre_id, gear_id, session=None):
    return session.query(Gear).filter_by(id=gear_id, centre_id=centre_id).first().total_quantity
