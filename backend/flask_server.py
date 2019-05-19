from flask import Flask, request, jsonify, make_response
from flask_restplus import Api, Resource, reqparse, fields
from flask_cors import CORS, cross_origin
from backend.login_register_delete import *

from flask_jwt_extended import (
    JWTManager,
    create_access_token,
    create_refresh_token,
    jwt_required
)

app = Flask(__name__)
app.config['BUNDLE_ERRORS'] = True
app.config['JWT_SECRET_KEY'] = 'jwt-secret-string'
api = Api(app=app, doc='/docs')
jwt = JWTManager(app)
app.config['JWT_QUERY_STRING_NAME'] = 'token'

ns_gear = api.namespace('gear', description='Operations on gear')
ns_user = api.namespace('user', description='Operations involving accounts')

@api.route('/test')
class testowa(Resource):
    @jwt_required
    def get(self):
        return "wygranko"

@api.route('/main')
class test123(Resource):
    def get(self):
        access_token = create_access_token(identity='janusz', fresh=True)
        refresh_token = create_refresh_token('janusz')
        return {
                   'access_token': access_token,
                   'refresh_token': refresh_token
               }, 200

    def post(self):
        return 'test ok post'


@ns_user.route('/registerUser')
class RegisterUser(Resource):
    resource_fields = api.model('registerUser', {
        'first_name': fields.String,
        'last_name': fields.String,
        'email': fields.String,
        'password': fields.String,
        'phone_number': fields.String,
    })
    parser = reqparse.RequestParser()
    parser.add_argument('first_name', type=str, required=True, help='First name of the user, e.g. John.')
    parser.add_argument('last_name', type=str, required=True, help='Last name of the user, e.g. Doe.')
    parser.add_argument('email', type=str, required=True, help='E-mail of the user, e.g. john.doe@gmail.com.')
    parser.add_argument('password', type=str, required=True, help='User password in plaintext.')
    parser.add_argument('phone_number', type=str, required=True, help='User phone number.')
    @api.doc(body=resource_fields)
    def post(self):
        args = self.parser.parse_args(strict=True)
        register_new_person(args['first_name'], args['last_name'], args['email'],
                            args['password'], args['phone_number'], role='User')
        return jsonify(True)


@ns_user.route('/loginUser')
class UserLogin(Resource):
    resource_fields = api.model('loginUser', {
        'email': fields.String,
        'password': fields.String,
    })
    @api.doc(body=resource_fields)
    def post(self):
        parser = reqparse.RequestParser()
        parser.add_argument('email', type=str, required=True, help='Email of the user logging in.')
        parser.add_argument('password', type=str, required=True, help='Password of the user logging in.')
        args = parser.parse_args(strict=True)
        login_user(args['email'], args['password'])

        return 'zaogowanyd'

# def user_login():
#     r = request.form
#     try:
#         email = r.get('email')
#         password = r.get('password')
#         login_user(email, password)
#         return "ok"
#     except Exception:
#         return "error in user_login()"
#

@app.route('/registerOwner', methods=['POST'])
@cross_origin(supports_credentials=True)
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
    except Exception:
        return "error in owner_user()"


# w sumie to jest to samo co loginUser, czy trzeba robic to inaczej? jakies print ze jako owner?
@app.route('/loginOwner', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def owner_login():
    r = request.form
    try:
        email = r.get('email')
        password = r.get('password')
        login_user(email, password)
        return "ok"
    except Exception:
        return "error in owner_login()"
# przetestowane wszystkie do tego miejsca
# nie powinno byc jeszcze enpointow z logout? 


# owner panel
# ta sama metoda dla ownera i usera
@app.route('/changeDataOwner', methods=['POST'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def data_change_owner():            # zakladam ze moze to zrobic tylko zalogowany owner
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
    except Exception:
        return "error in data_change_owner()"


@app.route('/addGearType', methods=['POST'])
@cross_origin(supports_credentials=True)
def add_gear_type():
    r = request.form
    try:
        name = r.get('name')
        price = r.get('price')
        centre_id = r.get('centre_id')
        gear_type_add(centre_id, name, price)
        return "ok"
    except Exception:
        return "error in add_gear_type()"


@app.route('/deleteGearType', methods=['POST'])
@cross_origin(supports_credentials=True)
def delete_gear_type():
    r = request.form
    try:
        name = r.get('name')
        centre_id = r.get('centre_id')
        gear_type_delete(centre_id, name)
        return "ok"
    except Exception:
        return "error in delete_gear_type()"


@app.route('/addGear', methods=['POST'])
@cross_origin(supports_credentials=True)
def add_gear():
    r = request.form
    try:
        quantity = r.get('quantity')
        centre_id = r.get('centre_id')
        name = r.get('name')
        gear_add(centre_id, name, quantity)
        return "ok"
    except Exception:
        return "error in add_gear()"


@app.route('/deleteGear', methods=['POST'])
@cross_origin(supports_credentials=True)
def delete_gear():
    r = request.form
    try:
        quantity = r.get('quantity')
        centre_id = r.get('centre_id')
        name = r.get('name')
        gear_delete(centre_id, name, quantity)
        return "ok"
    except Exception:
        return "error in delete_gear()"


# dla ownera
@app.route('/getGearOwner', methods=['GET'])
@cross_origin(supports_credentials=True)
def owner_get_gear():
    r = request.form
    try:
        centre_id = r.get('centre_id')
        gear_id = r.get('gear_id')
        get_gear_owner(centre_id, gear_id)
        return "ok"
    except Exception:
        return "error in owner_get_gear()"


@app.route('/getAllGear', methods=['GET'])
@cross_origin(supports_credentials=True)
def owner_get_all_gear():
    r = request.form
    try:
        centre_id = r.get('centre_id')
        all_gear_owner(centre_id)
        return "ok"
    except Exception:
        return "error in owner_get_all_gear()"


# client panel
@app.route('/changeDataClient', methods=['POST'])
@cross_origin(supports_credentials=True)
def data_change():            # bierzemy id bo nie mozemy usera o to pytac
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
    except Exception:
        return "error in data_change()"


@app.route('/getGearClient', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def get_gear():
    r = request.form
    try:
        gear_id = r.get('gear_id')
        get_gear_client(gear_id)
        return "ok"
    except Exception:
        return "error in get_gear()"


@app.route('/getAllGear', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def get_all_gear():
    try:
        all_gear_client()
        return "ok"
    except Exception:
        return "error in get_all_gear()"


@app.route('/rentGear', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def rent_gear():
    r = request.form
    try:
        user_id = r.get('user_id')
        centre_id = r.get('centre_id')
        gear_id = r.get('gear_id')
        start = r.get('start')
        end = r.get('end')
        rent_amount = r.get('rent_amount')
        gear_rent(user_id, gear_id, centre_id, start, end, rent_amount)
        return "ok"
    except Exception:  # tak sie nie do konca powinno robic. Jak sie wyjebie keyword to powinno sie
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
    except Exception:
        return "error in all_centre_gear_get()"


@app.route('/getAllUserRentals', methods=['GET'])
@cross_origin(supports_credentials=True)  # to jest potrzebne
def all_user_rentals_get():
    r = request.form
    try:
        user_id = r.get('user_id')
        get_all_user_rentals(user_id)
        return "ok"
    except Exception:
        return "error in all_centre_gear_get()"


if __name__ == '__main__':
    app.run(debug=True)
