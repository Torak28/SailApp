from database.insert import *
from backend.login_register_delete import register_new_person
role1 = create_role('User')
role2 = create_role('Admin')
add_object_to_database(role1)
add_object_to_database(role2)
register_new_person('Jan', 'Kowalski', 'jan.kowalski@test.com', 'testowe_haslo', '500101900')
register_new_person('Przemysław', 'Schrekwentke', 'swamp.admin@test.com', 'haslo_admina', '500600700', 'Admin')

