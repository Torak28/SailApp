from database.create_objects_of_classes import create_rental as create_rent
from database.create_objects_of_classes import add_object_to_database
from database.database_classes import connection_to_db, GearRental, User, WaterCentre, Gear
import datetime
from datetimerange import DateTimeRange
from pprint import pprint


def create_rental(user_id, centre_id, gear_id, rent_amount, rent_start, rent_end, is_accepted):
    rental = create_rent(user_id, gear_id, centre_id, rent_start, rent_end, rent_amount, is_accepted)
    add_object_to_database(rental)


@connection_to_db
def delete_rental(rental_id, session=None):
    session.query(GearRental).filter_by(id=rental_id).delete()


@connection_to_db
def edit_rental(rent_id, kwargs, session=None):
    session.query(GearRental).filter_by(id=rent_id).update(kwargs)


@connection_to_db
def is_user_allowed_to_delete_rental(user_id, rental_id, session=None):
    rental = session.query(GearRental).filter_by(id=rental_id).first()
    centre_id = rental.centre_id
    if rental.user_id == user_id:  # if user wanting to delete is the real owner of the rental
        return True
    elif session.query(User).filter_by(id=user_id).first().id == user_id:  # if user wanting to delete is centre owner
        return True
    return False


@connection_to_db
def is_user_rent_owner(user_id, rental_id, session=None):
    rental = session.query(GearRental).filter_by(id=rental_id).first()
    return True if rental.user_id == user_id else False

@connection_to_db
def get_rentals_by_water_centre_id(centre_id, session=None):
    now = datetime.datetime.now()
    rentals = session.query(WaterCentre, GearRental, Gear).filter(WaterCentre.id == centre_id,
                                                                  GearRental.rent_end > now,
                                                                  Gear.id == GearRental.gear_id).all()
    list_of_formatted_rentals = []
    for rental in rentals:
        formatted_rental = dict()
        formatted_rental['gear_id'] = rental.Gear.id
        formatted_rental['gear_name'] = rental.Gear.name
        formatted_rental['rent_id'] = rental.GearRental.id
        formatted_rental['rent_start'] = rental.GearRental.rent_start
        formatted_rental['rent_end'] = rental.GearRental.rent_end
        formatted_rental['rent_quantity'] = rental.GearRental.rent_amount
        list_of_formatted_rentals.append(formatted_rental)
    return list_of_formatted_rentals


@connection_to_db
def get_rentals_for_centre_owner(centre_id, session=None):
    rentals = get_rentals_by_water_centre_id(centre_id)

    for rental in rentals:
        renting_person = session.query(User, GearRental).filter(GearRental.id == rental['rent_id'],
                                                                GearRental.user_id == User.id).first()
        rental['first_name'] = renting_person.User.first_name
        rental['last_name'] = renting_person.User.last_name
        rental['email'] = renting_person.User.email
        rental['phone_number'] = renting_person.User.phone_number
        pprint(rental)
    return rentals


@connection_to_db
def get_centre_id(rental_id, session=None):
    return session.query(GearRental).filter_by(id=rental_id).first().centre_id


@connection_to_db
def check_if_rent_is_possible(centre_id, gear_id, rent_amount, rent_start, rent_end, max_rent, session=None):
    rent_start = datetime.datetime.strptime(rent_start, '%Y-%m-%dT%H:%M:%S.%fZ')
    rent_end = datetime.datetime.strptime(rent_end, '%Y-%m-%dT%H:%M:%S.%fZ')
    all_rents = session.query(GearRental).filter_by(centre_id=centre_id, gear_id=gear_id).filter(
        GearRental.rent_end > rent_start,
        GearRental.rent_start < rent_end,
        GearRental.rent_status != -1).all()
    iter_start = rent_start
    while iter_start <= rent_end:
        total_number_of_rented_gear = 0
        iter_start_with_delta = iter_start + datetime.timedelta(minutes=15)
        timerange = DateTimeRange(iter_start, iter_start_with_delta)
        for rent in all_rents:
            rent_time_range = DateTimeRange(rent.rent_start, rent.rent_end)
            if timerange.intersection(rent_time_range):
                total_number_of_rented_gear += rent.rent_amount
        if total_number_of_rented_gear + rent_amount > max_rent:
            return False
        iter_start = iter_start_with_delta
    return True


@connection_to_db
def get_all_pending_rentals(owner_id, session=None):
    pending_rentals = session.query(GearRental, WaterCentre, Gear).filter(GearRental.rent_status == 0,
                                                                          WaterCentre.owner_id == owner_id,
                                                                          GearRental.gear_id == Gear.id,
                                                                          GearRental.centre_id == WaterCentre.id).all()
    list_of_formatted_rentals = []
    for rental in pending_rentals:
        formatted_rental = dict()
        formatted_rental['gear_id'] = rental.Gear.id
        formatted_rental['gear_name'] = rental.Gear.name
        formatted_rental['rent_id'] = rental.GearRental.id
        formatted_rental['rent_start'] = rental.GearRental.rent_start
        formatted_rental['rent_end'] = rental.GearRental.rent_end
        formatted_rental['rent_quantity'] = rental.GearRental.rent_amount
        formatted_rental['centre_name'] = rental.WaterCentre.name
        formatted_rental['centre_id'] = rental.WaterCentre.id
        list_of_formatted_rentals.append(formatted_rental)
    return list_of_formatted_rentals


@connection_to_db
def decide_about_rental(rental_id, decision, session=None):
    owner = session.query(GearRental).filter_by(id=rental_id).first()
    owner.rent_status = decision
    session.commit()
