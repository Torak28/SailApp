from database.create_objects_of_classes import create_role, create_centre, \
    add_object_to_database, create_gear, create_rental
from database.database_classes import recreate_database
from backend.login_register_delete import register_new_person
import datetime


def prepare_db():
    recreate_database()
    role1 = create_role('User')
    role2 = create_role('Admin')
    role3 = create_role('Owner')

    add_object_to_database(role1)
    add_object_to_database(role2)
    add_object_to_database(role3)

    register_new_person('Jan', 'Kowalski', 'jan.kowalski@test.com', 'testowe_haslo', '500101900', 'user')
    register_new_person('Przemysław', 'Szczurzewski', 'swamp.owner@test.com', 'haslo_ownera', '500600700', 'owner')
    register_new_person('Ross', 'Ulbricht', 'admin@test.com', 'haslo_admina', '500600420', 'admin')
    register_new_person('Jerzy', 'Dudek', 'jerzy.dudek@test.com', 'testowe_haslo', '700800299', 'user')

    centre1 = create_centre(2, 'Wodna Nuta', '50.3483816', '18.9157175', '500600701')
    centre2 = create_centre(2, 'Melodia', '50.3413816', '18.3157175', '773000109')
    centre1_1 = create_gear(1, 'Kajak', '10', '15')
    centre1_2 = create_gear(1, 'Rower wodny', '30', '12')
    add_object_to_database(centre1)
    add_object_to_database(centre2)
    add_object_to_database(centre1_1)
    add_object_to_database(centre1_2)

    now = datetime.datetime.now()
    future_datetime = now + datetime.timedelta(hours=3)
    more_future_datetime = now + datetime.timedelta(hours=6)
    past_datetime = now - datetime.timedelta(hours=3)
    rental1 = create_rental(1, 1, 1, now, future_datetime, 1, True)  # Jan Kowalski kajak Wodna_Nuta 1szt. W TRAKCIE
    rental2 = create_rental(3, 1, 1, now, future_datetime, 1, True)  # Jerzy Dudek kajak Wodna_Nuta 1szt. W TRAKCIE
    rental3 = create_rental(1, 1, 1, past_datetime, now, 2, True)  # Jan Kowalski kajak Wodna_Nuta 2szt.  STARE
    rental4 = create_rental(3, 1, 1, past_datetime, future_datetime, 3, False)  # Jerzy Dudek kajak Wodna_Nuta 3szt.  W TRAKCIE
    rental5 = create_rental(1, 1, 1, future_datetime, more_future_datetime, 3, False)  # Jan Kowalski kajak Wodna_Nuta 3szt. PRZYSZ
    add_object_to_database(rental1)
    add_object_to_database(rental2)
    add_object_to_database(rental3)
    add_object_to_database(rental4)
    add_object_to_database(rental5)


# prepare_db()