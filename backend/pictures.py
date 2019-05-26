import os
from database.database_classes import connection_to_db, Picture
from database.create_objects_of_classes import create_picture, add_object_to_database

def get_current_path():
    return os.path.dirname(os.path.abspath(__file__))


def save_file(file):
    i = 1
    filepath = os.path.join(get_current_path(), 'pictures', file.filename)
    new_filepath = filepath
    while True:
        if os.path.isfile(new_filepath):
            i += 1
            new_filepath = ''.join(filepath.split('.')[:-1]) + str(i) + '.' + str(filepath.split('.')[-1])
            continue
        break
    file.save(new_filepath)
    return filepath


def add_picture_to_centre(centre_id, filepath):
    picture = create_picture(filepath, centre_id)
    add_object_to_database(picture)
