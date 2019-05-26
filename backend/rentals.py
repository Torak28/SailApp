from database.create_objects_of_classes import create_rental as create_rent
from database.create_objects_of_classes import add_object_to_database
from database.database_classes import connection_to_db, GearRental, User, WaterCentre, Gear
import datetime
from pprint import pprint


def create_rental(user_id, centre_id, gear_id, rent_amount, rent_start, rent_end):
    rental = create_rent(user_id, gear_id, centre_id, rent_start, rent_end, rent_amount)
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
