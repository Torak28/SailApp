from database.create_objects_of_classes import create_role, create_centre, \
    add_object_to_database, create_gear, create_rental
from database.database_classes import recreate_database
from backend.login_register_delete import register_new_person
import datetime

recreate_database()
role1 = create_role('User')
role2 = create_role('Admin')
role3 = create_role('Owner')

add_object_to_database(role1)
add_object_to_database(role2)
add_object_to_database(role3)

register_new_person('Jan', 'Kowalski', 'jan.kowalski@test.com', 'testowe_haslo', '500101900', 'user')
register_new_person('Przemysław', 'Schrekwentke', 'swamp.admin@test.com', 'haslo_ownera', '500600700', 'owner')

centre1 = create_centre(2, 'Wodna Nuta', '2137', '420', '500600701')
centre2 = create_gear(1, 'Kajak', '10', '15')
centre3 = create_gear(1, 'Rower wodny', '30', '12')
add_object_to_database(centre1)
add_object_to_database(centre2)
add_object_to_database(centre3)

now = datetime.datetime.now()
future_datetime = now + datetime.timedelta(days=3)
past_datetime = now - datetime.timedelta(days=3)
rental1 = create_rental(1, 1, 1, now, future_datetime, 1)
rental2 = create_rental(1, 1, 1, past_datetime, now, 2)
rental3 = create_rental(1, 1, 1, past_datetime, future_datetime, 3)
add_object_to_database(rental1)
add_object_to_database(rental2)
add_object_to_database(rental3)


