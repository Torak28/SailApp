from database.create_objects_of_classes import create_role, create_centre, add_object_to_database, create_gear
from database.database_classes import recreate_database
from backend.login_register_delete import register_new_person
recreate_database()
role1 = create_role('User')
role2 = create_role('Admin')
role3 = create_role('Owner')

add_object_to_database(role1)
add_object_to_database(role2)
add_object_to_database(role3)

register_new_person('Jan', 'Kowalski', 'jan.kowalski@test.com', 'testowe_haslo', '500101900', 'user')
register_new_person('Przemysław', 'Schrekwentke', 'swamp.admin@test.com', 'haslo_admina', '500600700', 'owner')

create_centre(2, 'Wodna Nuta', '2137', '420')
create_gear(1, 'Kajak', '10', '15')
create_gear(1, 'Rower wodny', '30', '12')
