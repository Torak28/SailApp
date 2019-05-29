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


@connection_to_db
def get_picture(picture_id, session=None):
    picture = session.query(Picture).filter_by(id=picture_id).first()
    return picture.file_path


@connection_to_db
def get_pictures_ids_of_centre(centre_id, session=None):
    ids = session.query(Picture).filter_by(water_centre_id=centre_id).all()
    list_of_formatted_ids = []
    for one_id in ids:
        list_of_formatted_ids.append({'picture_id': one_id.id})
    return list_of_formatted_ids
