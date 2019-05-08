from database.create_objects_of_classes import create_role, create_user, add_object_to_database

role1 = create_role('User')
role2 = create_role('Admin')
role3 = create_role('Owner')

add_object_to_database(role1)
add_object_to_database(role2)
add_object_to_database(role3)

user1 = create_user('Jan', 'Kowalski', 'jan.kowalski@test.com', 'testowe_haslo', '500101900', '1')
user2 = create_user('Przemysław', 'Schrekwentke', 'swamp.admin@test.com', 'haslo_admina', '500600700', '1')

add_object_to_database(user1)
add_object_to_database(user2)
