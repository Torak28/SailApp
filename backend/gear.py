from database.create_objects_of_classes import create_gear, add_object_to_database


def add_gear(centre_id, gear_name, gear_price, gear_quantity):
    gear = create_gear(centre_id, gear_name, gear_quantity, gear_price)
    add_object_to_database(gear)
