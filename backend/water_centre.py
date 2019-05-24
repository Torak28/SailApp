from database.create_objects_of_classes import create_centre, add_object_to_database


def add_water_centre(owner_id, centre_name, centre_latitude, centre_longitude):
    centre = create_centre(owner_id, centre_name, centre_latitude, centre_longitude)
    add_object_to_database(centre)

