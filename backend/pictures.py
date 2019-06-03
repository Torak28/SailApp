import os
from database.database_classes import connection_to_db, Picture
from database.create_objects_of_classes import create_picture, add_object_to_database
from imgurpython import ImgurClient


def get_current_path():
    return os.path.dirname(os.path.abspath(__file__))


def send_file_to_imgur(file):
    client_id = 'aa5de24b298c96e'
    client_secret = '9adf1eead953b72c004d6a7540e91e1480298c91'
    client = ImgurClient(client_id, client_secret)
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
    response_data = client.upload_from_path(new_filepath)
    os.remove(new_filepath)
    return response_data['link']


def add_picture_to_centre(centre_id, filepath):
    picture = create_picture(filepath, centre_id)
    add_object_to_database(picture)


@connection_to_db
def get_picture(picture_id, session=None):
    picture = session.query(Picture).filter_by(id=picture_id).first()
    return picture.file_path


@connection_to_db
def get_pictures_ids_and_links_of_centre(centre_id, session=None):
    pictures = session.query(Picture).filter_by(water_centre_id=centre_id).all()
    list_of_formatted_data = []
    for picture in pictures:
        list_of_formatted_data.append({'picture_id': picture.id,
                                       'picture_link': picture.file_path})
    return list_of_formatted_data

