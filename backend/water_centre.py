from database.create_objects_of_classes import create_centre, add_object_to_database
from database.database_classes import connection_to_db, WaterCentre

def add_water_centre(owner_id, centre_name, centre_latitude, centre_longitude, telephone_no):
    centre = create_centre(owner_id, centre_name, centre_latitude, centre_longitude, telephone_no)
    add_object_to_database(centre)


@connection_to_db
def get_water_centre_by_id(centre_id, session=None):
    centre = session.query(WaterCentre).filter_by(id=centre_id).first()
    if centre:
        formatted_centre = dict()
        formatted_centre['centre_id'] = centre_id
        formatted_centre['latitude'] = centre.latitude
        formatted_centre['longitude'] = centre.longitude
        formatted_centre['name'] = centre.name
        formatted_centre['phone_number'] = centre.contact_number
        return formatted_centre
    return None


@connection_to_db
def get_water_centres_by_owner_id(owner_id, session=None):
    centres = session.query(WaterCentre).filter_by(owner_id=owner_id).all()
    list_of_centres = []
    for centre in centres:
        formatted_centre = get_water_centre_by_id(centre.id)
        list_of_centres.append(formatted_centre)
    return list_of_centres
