from flask import Flask, request, jsonify
from flask_cors import CORS, cross_origin
from passlib.hash import sha256_crypt as crypter
from backend.login_register_delete import register_new_person, login_user, register_new_owner, change_data, change_data_rental, gear_type_add, gear_type_delete, gear_add, gear_delete, get_gear_owner, all_gear_owner, get_gear_client, all_gear_client, gear_rent

app = Flask(__name__, static_url_path='')


#main page
#przetestowane od endpointy od tego miejsca
@app.route('/registerUser', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def create_user_account():
    r = request.form
    try:
        first_name = r.get('first_name')
        last_name = r.get('last_name')
        email = r.get('email')
        password = r.get('password')
        phone_number = r.get('phone_number')
        register_new_person(first_name, last_name, email, password, phone_number)
        return "ok"
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return "error in create_user_account()"


@app.route('/loginUser', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def user_login():
    r = request.form
    try:
        email = r.get('email')
        password = r.get('password')
        login_user(email, password)
        return "ok"
    except Exception as e:
        return "error in user_login()"


@app.route('/registerOwner', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def owner_user():
    r = request.form
    try:
        first_name = r.get('first_name')
        last_name = r.get('last_name')
        email = r.get('email')
        password = r.get('password')
        phone_number = r.get('phone_number')
        register_new_owner(first_name, last_name, email, password, phone_number)
        return "ok"
    except Exception as e:
        return "error in owner_user()"

#w sumie to jest to samo co loginUser, czy trzeba robic to inaczej? jakies print ze jako owner?
@app.route('/loginOwner', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def owner_login():
    r = request.form
    try:
        email = r.get('email')
        password = r.get('password')
        login_user(email, password)
        return "ok"
    except Exception as e:
        return "error in owner_login()"
#przetestowane wszystkie do tego miejsca
# nie powinno byc jeszcze enpointow z logout? 

#owner panel
#ta sama metoda dla ownera i usera
@app.route('/changeDataOwner', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def data_change_owner():            #zakladam ze moze to zrobic tylko zalogowany owner
    r = request.form
    try:
        first_name = r.get('first_name')
        last_name = r.get('last_name')
        email = r.get('email')
        phone_number = r.get('phone_number')
        password = r.get('password')
        user_id=r.get('user_id')
        change_data(user_id, first_name, last_name, email, phone_number, password)
        centre_id=r.get('centre_id')
        name = r.get('name')
        latitude = r.get('latitude')
        longitude = r.get('longitude')
        change_data_rental(centre_id,name,latitude,longitude)
        return "ok"
    except Exception as e:
        return "error in data_change_owner()"


@app.route('/addGearType', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def add_gear_type():
    r = request.form
    try:
        name=r.get('name')
        price=r.get('price')
        centre_id=r.get('centre_id')
        gear_type_add(centre_id, name, price)
        return "ok"
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return "error in add_gear_type()"


@app.route('/deleteGearType', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def delete_gear_type():
    r = request.form
    try:
        name=r.get('name')
        centre_id=r.get('centre_id')
        gear_type_delete(centre_id, name)
        return "ok"
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return "error in delete_gear_type()"


@app.route('/addGear', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def add_gear():
    r = request.form
    try:
        quantity=r.get('quantity')
        centre_id=r.get('centre_id')
        name=r.get('name')
        gear_add(centre_id, name, quantity)
        return "ok"
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return "error in add_gear()"


@app.route('/deleteGear', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def delete_gear():
    r = request.form
    try:
        quantity=r.get('quantity')
        centre_id=r.get('centre_id')
        name=r.get('name')
        gear_delete(centre_id, name, quantity)
        return "ok"
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return "error in delete_gear()"


#dla ownera
@app.route('/getGearOwner', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def owner_get_gear():
    try:
        centre_id=r.get('centre_id')
        gear_id=r.get('gear_id')
        get_gear_owner(centre_id,gear_id)
        return "ok"
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return "error in owner_get_gear()"


@app.route('/getAllGear', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def owner_get_all_gear():
    try:
        centre_id=r.get('centre_id')
        all_gear_owner(centre_id)
        return "ok"
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return "error in owner_get_all_gear()"


#client panel
@app.route('/changeDataClient', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def data_change():            #bierzemy id bo nie mozemy usera o to pytac
    r = request.form
    try:
        first_name = r.get('first_name')
        last_name = r.get('last_name')
        email = r.get('email')
        phone_number = r.get('phone_number')
        password = r.get('password')
        user_id=r.get('user_id')
        change_data(user_id, first_name, last_name, email, phone_number, password)
        return "ok"
    except Exception as e:
        return "error in data_change()"


@app.route('/getGearClient', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def get_gear():
    try:
        gear_id=r.get(gear_id)
        get_gear_client(gear_id)
        return "ok"
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return "error in get_gear()"


@app.route('/getAllGear', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def get_all_gear():
    try:
        all_gear_client()
        return "ok"
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return "error in get_all_gear()"


@app.route('/rentGear', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def rent_gear():
    r = request.form
    try:
        user_id=r.get('user_id')
        centre_id=r.get('centre_id')
        gear_id=r.get('gear_id')
        start=r.get('start')
        end=r.get('end')
        rent_amount=r.get('rent_amount')
        gear_rent(user_id, gear_id, centre_id, start, end, rent_amount)
        return "ok"
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return "error in rent_gear()"


@app.route('/getCentres', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def centres_get():
    try:
        get_all_centres()
        return "ok"
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return "error in centres_get()"


@app.route('/getAllCentreGear', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def all_centre_gear_get():
    r = request.form
    try:
        centre_id = r.get('centre_id')
        get_all_centre_gear(centre_id)
        return "ok"
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return "error in all_centre_gear_get()"


@app.route('/getAllUserRentals', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def all_user_rentals_get():
    r = request.form
    try:
        user_id = r.get('user_id')
        get_all_user_rentals(user_id)
        return "ok"
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return "error in all_centre_gear_get()"


if __name__ == '__main__':
    app.run(debug=True)
