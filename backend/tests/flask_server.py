from flask import Flask, request, jsonify
from flask_cors import CORS, cross_origin
from passlib.hash import sha256_crypt as crypter
from backend.login_register_delete import register_new_person, login_user, register_new_owner, change_data, change_data_rental, gear_type_add, gear_type_delete, gear_add, gear_delete, get_gear_owner, all_gear_owner, get_gear_client, all_gear_client, rent_gear

app = Flask(__name__, static_url_path='')


#main page
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
        return True
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return jsonify(e)


@app.route('/loginUser', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def user_login():
    r = request.form
    try:
        email = r.get('email')
        password = r.get('password')
        login_user(email, password)
        return True
    except Exception as e:
        return jsonify(e)


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
        return True
    except Exception as e:
        return jsonify(e)

#w sumie to jest to samo co loginUser, czy trzeba robic to inaczej? jakies print ze jako owner?
@app.route('/loginOwner', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def owner_login():
    r = request.form
    try:
        email = r.get('email')
        password = r.get('password')
        login_user(email, password)
        return True
    except Exception as e:
        return jsonify(e)


#owner panel
#ta sama metoda dla ownera i usera
@app.route('/changeData', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def data_change(id, centre_id):            #bierzemy id bo nie mozemy usera o to pytac
    r = request.form
    try:
        first_name = r.get('first_name')
        last_name = r.get('last_name')
        email = r.get('email')
        phone_number = r.get('phone_number')
        password = r.get('password')
        change_data(id, first_name, last_name, email, phone_number, password)
        name = r.get('name')
        latitude = r.get('latitude')
        longitude = r.get('longitude')
        change_data_rental(centre_id,name,latitude,longitude)
        return True
    except Exception as e:
        return jsonify(e)


@app.route('/addGearType', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def add_gear_type(centre_id):
    r = request.form
    try:
        name=r.get('name')
        price=r.get('price')
        gear_type_add(centre_id, name, price)
        return True
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return jsonify(e)


@app.route('/deleteGearType', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def delete_gear_type(centre_id):
    r = request.form
    try:
        name=r.get('name')
        gear_type_delete(centre_id, name)
        return True
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return jsonify(e)


@app.route('/addGear', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def add_gear(centre_id, name):
    r = request.form
    try:
        quantity=r.get('quantity')
        gear_add(centre_id, name, quantity)
        return True
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return jsonify(e)


@app.route('/deleteGear', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def delete_gear(centre_id, name):
    r = request.form
    try:
        quantity=r.get('quantity')
        gear_delete(centre_id, name, quantity)
        return True
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return jsonify(e)


#dla ownera
@app.route('/getGear', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def get_gear(centre_id,id):
    get_gear_owner(centre_id,id)
    return True
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return jsonify(e)


@app.route('/getAllGear', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def get_all_gear(centre_id):
    all_gear_owner(centre_id)
    return True
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return jsonify(e)


#client panel
@app.route('/changeData', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def data_change(id):            #bierzemy id bo nie mozemy usera o to pytac
    r = request.form
    try:
        first_name = r.get('first_name')
        last_name = r.get('last_name')
        email = r.get('email')
        phone_number = r.get('phone_number')
        password = r.get('password')
        change_data(id, first_name, last_name, email, phone_number, password)
        return True
    except Exception as e:
        return jsonify(e)


@app.route('/getGear', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def get_gear(gear_id):
    get_gear_client(gear_id)
    return True
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return jsonify(e)


@app.route('/getAllGear', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def get_all_gear():
    all_gear_client()
    return True
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return jsonify(e)


@app.route('/rentGear', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def rent_gear(id, gear_id, centre_id):
    r = request.form
    try:
        start=r.get('start')
        end=r.get('end')
        rent_amount=r.get('rent_amount')
        gear_rent(id, gear_id, centre_id, start, end, rent_amount)
        return True
    except Exception as e:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
                            # zwracac czytelny blad
        return jsonify(e)


if __name__ == '__main__':
    app.run(debug=True)
