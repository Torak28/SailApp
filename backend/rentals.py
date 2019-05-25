from database.create_objects_of_classes import create_rental as create_rent
from database.database_classes import connection_to_db, GearRental, User, WaterCentre
import datetime


def create_rental(user_id, centre_id, gear_id, rent_amount, rent_start, rent_end):
    create_rent(user_id, gear_id, centre_id, rent_start, rent_end, rent_amount)


@connection_to_db
def delete_rental(rental_id, session=None):
    session.query(GearRental).filter_by(id=rental_id).delete()


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
def get_rentals_by_water_centre_id(centre_id, session=None):
    now = datetime.datetime.now()
    rentals = session.query(WaterCentre, GearRental).filter(WaterCentre.id == centre_id,
                                                            GearRental.rent_end > now).all()
    for rental in rentals:
        pass
