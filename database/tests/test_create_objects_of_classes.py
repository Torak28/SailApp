from database.create_objects_of_classes import *

role1 = create_role('User')
role2 = create_role('Admin')
user = create_user('Papa', 'Arab', 'jpnasto@gmail.com', 'chuj', '123123123', '2')

add_object_to_database(role1)
add_object_to_database(role2)
add_object_to_database(user)
